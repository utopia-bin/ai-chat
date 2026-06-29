package cn.scws.ai.chat.tencent.v1;

import cn.scws.ai.chat.tencent.support.AbstractTencentChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * V1 Model Of Tencent Knowledge Engine
 */
public class TencentV1ChatModel extends AbstractTencentChatModel<TencentV1ChatOptions, TencentV1Api.ChatResponseChunk> {
    private final TencentV1Api tencentV1Api;

    public TencentV1ChatModel(TencentV1Api tencentV1Api, TencentV1ChatOptions defaultOptions) {
        super(defaultOptions, TencentV1ChatOptions.class);
        Assert.notNull(tencentV1Api, "Tencent api cannot be null");
        this.tencentV1Api = tencentV1Api;
    }

    @Override
    protected Flux<TencentV1Api.ChatResponseChunk> doStream(Prompt prompt, TencentV1ChatOptions options) {
        return this.tencentV1Api.chatCompletionStream(this.createRequest(prompt, options));
    }

    TencentV1Api.ChatRequest createRequest(Prompt prompt, TencentV1ChatOptions options) {
        String systemRole = prompt.getInstructions().stream()
                .filter(message -> message.getMessageType() == MessageType.SYSTEM)
                .map(Message::getText)
                .findFirst()
                .orElse(null);

        String userContent = prompt.getInstructions().stream()
                .filter(message -> message.getMessageType() == MessageType.USER)
                .map(Message::getText)
                .collect(Collectors.joining("\n"));

        return ModelOptionsUtils.merge(options,
                TencentV1Api.ChatRequest.builder()
                        .content(userContent)
                        .systemRole(systemRole)
                        .build(),
                TencentV1Api.ChatRequest.class);
    }

    @Override
    protected ChatResponse toChatResponse(TencentV1Api.ChatResponseChunk chunk) {
        String content = "";
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("type", chunk.type());

        if (chunk.payload() != null) {
            metadata.putAll(chunk.payload());
            if (TencentV1EventEnums.Reply.getType().equals(chunk.type())) {
                TencentV1ChatEvent.Reply reply = ModelOptionsUtils.jsonToObject(
                        ModelOptionsUtils.toJsonString(chunk.payload()),
                        TencentV1ChatEvent.Reply.class
                );
                if (reply.content() != null) {
                    content = reply.content();
                }
            }
        }

        if (chunk.error() != null) {
            metadata.put("error", chunk.error());
        }
        if (chunk.requestId() != null) {
            metadata.put("requestId", chunk.requestId());
        }

        return this.createChatResponse(content, metadata);
    }
}
