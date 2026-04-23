package cn.scws.ai.tencent.v2;

import cn.scws.ai.tencent.support.AbstractTencentChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * V2 Model Of Tencent Knowledge Engine
 */
public class TencentV2ChatModel extends AbstractTencentChatModel<TencentV2ChatOptions, TencentV2Api.ChatResponseChunk> {
    private final TencentV2Api tencentV2Api;

    public TencentV2ChatModel(TencentV2Api tencentV2Api, TencentV2ChatOptions defaultOptions) {
        super(defaultOptions, TencentV2ChatOptions.class);
        Assert.notNull(tencentV2Api, "Tencent api cannot be null");
        this.tencentV2Api = tencentV2Api;
    }

    @Override
    protected Flux<TencentV2Api.ChatResponseChunk> doStream(Prompt prompt, TencentV2ChatOptions options) {
        return this.tencentV2Api.chatCompletionStream(this.createRequest(prompt, options));
    }

    TencentV2Api.ChatRequest createRequest(Prompt prompt, TencentV2ChatOptions options) {
        String systemRole = prompt.getInstructions().stream()
                .filter(message -> message.getMessageType() == MessageType.SYSTEM)
                .map(Message::getText)
                .findFirst()
                .orElse(null);

        List<TencentV2Api.Content> contents = prompt.getInstructions().stream()
                .flatMap(message -> {
                    if (message instanceof UserMessage userMessage) {
                        return this.convertUserMessage(userMessage).stream();
                    }
                    return Stream.empty();
                })
                .toList();

        return ModelOptionsUtils.merge(options,
                TencentV2Api.ChatRequest.builder()
                        .contents(contents)
                        .systemRole(systemRole)
                        .build(),
                TencentV2Api.ChatRequest.class);
    }

    List<TencentV2Api.Content> convertUserMessage(UserMessage message) {
        List<TencentV2Api.Content> contents = new ArrayList<>();

        if (StringUtils.hasText(message.getText())) {
            contents.add(new TencentV2Api.Content(message.getText()));
        }

        if (!CollectionUtils.isEmpty(message.getMedia())) {
            message.getMedia().forEach(media -> {
                String type = media.getMimeType().getType();
                if ("image".equalsIgnoreCase(type)) {
                    contents.add(new TencentV2Api.Content(new TencentV2Api.Image((String) media.getData())));
                }
            });
        }

        Map<String, Object> metadata = message.getMetadata();
        if (!CollectionUtils.isEmpty(metadata)) {
            if (metadata.get("CustomVariables") instanceof Map<?, ?> customVars) {
                Map<String, String> variables = new HashMap<>();
                customVars.forEach((key, value) -> variables.put(String.valueOf(key), String.valueOf(value)));
                contents.add(new TencentV2Api.Content(variables));
            }
            if (metadata.get("WidgetAction") instanceof TencentV2Api.WidgetAction action) {
                contents.add(new TencentV2Api.Content(action));
            }
        }

        return contents;
    }

    @Override
    protected ChatResponse toChatResponse(TencentV2Api.ChatResponseChunk chunk) {
        String eventType = chunk.event();
        String content = "";

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("type", eventType);
        if (chunk.data() != null) {
            metadata.putAll(chunk.data());
        }

        if (chunk.data() != null) {
            if (TencentV2EventEnums.TextDelta.getType().equals(eventType)) {
                TencentV2ChatEvent.TextDelta delta = ModelOptionsUtils.jsonToObject(
                        ModelOptionsUtils.toJsonString(chunk.data()),
                        TencentV2ChatEvent.TextDelta.class
                );
                if (delta.text() != null) {
                    content = delta.text();
                }
            } else if (TencentV2EventEnums.TextReplace.getType().equals(eventType)) {
                TencentV2ChatEvent.TextReplace replace = ModelOptionsUtils.jsonToObject(
                        ModelOptionsUtils.toJsonString(chunk.data()),
                        TencentV2ChatEvent.TextReplace.class
                );
                if (replace.text() != null) {
                    content = replace.text();
                }
            } else if (TencentV2EventEnums.ResponseCompleted.getType().equals(eventType)) {
                TencentV2ChatEvent.ResponseCompleted completed = ModelOptionsUtils.jsonToObject(
                        ModelOptionsUtils.toJsonString(chunk.data()),
                        TencentV2ChatEvent.ResponseCompleted.class
                );
                content = this.extractCompletedText(completed);
                if (!content.isEmpty()) {
                    metadata.put("finalText", content);
                }
            }
        }

        return this.createChatResponse(content, metadata);
    }

    private String extractCompletedText(TencentV2ChatEvent.ResponseCompleted completed) {
        if (completed == null || completed.record() == null || completed.record().messages() == null) {
            return "";
        }

        return completed.record().messages().stream()
                .filter(message -> "reply".equals(message.type()))
                .findFirst()
                .flatMap(message -> message.contents()
                        .stream()
                        .filter(content -> "text".equals(content.type()))
                        .findFirst())
                .map(TencentV2Api.Content::text)
                .orElse("");
    }
}
