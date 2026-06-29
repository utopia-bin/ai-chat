package cn.scws.ai.chat.tencent.support;

import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("NullableProblems")
public abstract class AbstractTencentChatModel<T extends ChatOptions, C> implements ChatModel {
    private final T defaultOptions;

    private final Class<T> optionsType;

    protected AbstractTencentChatModel(T defaultOptions, Class<T> optionsType) {
        Assert.notNull(defaultOptions, "default options cannot be null");
        Assert.notNull(optionsType, "optionsType cannot be null");
        this.defaultOptions = defaultOptions;
        this.optionsType = optionsType;
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        return this.stream(prompt)
                .collectList()
                .map(this::resolveFinalResponse)
                .blockOptional()
                .orElseGet(() -> this.emptyResponse(Map.of("type", "empty")));
    }

    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
        T options = this.mergeOptions(prompt.getOptions());
        return this.doStream(prompt, options).map(this::toChatResponse);
    }

    protected T mergeOptions(ChatOptions runtimeOptions) {
        if (runtimeOptions == null) {
            return this.defaultOptions.copy();
        }
        if (this.optionsType.isInstance(runtimeOptions)) {
            return ModelOptionsUtils.merge(this.optionsType.cast(runtimeOptions), this.defaultOptions, this.optionsType);
        }
        Map<String, Object> runtimeMap = ModelOptionsUtils.objectToMap(runtimeOptions);
        return ModelOptionsUtils.merge(runtimeMap, this.defaultOptions, this.optionsType);
    }

    protected abstract Flux<C> doStream(Prompt prompt, T options);

    protected abstract ChatResponse toChatResponse(C chunk);

    protected ChatResponse emptyResponse(Map<String, Object> metadata) {
        return this.createChatResponse("", metadata);
    }

    protected ChatResponse createChatResponse(String content, Map<String, Object> metadata) {
        AssistantMessage assistantMessage = AssistantMessage.builder()
                .content(Objects.requireNonNullElse(content, ""))
                .properties(metadata)
                .build();
        return new ChatResponse(List.of(new Generation(assistantMessage, ChatGenerationMetadata.NULL)));
    }

    private ChatResponse resolveFinalResponse(List<ChatResponse> responses) {
        if (responses.isEmpty()) {
            return this.emptyResponse(Map.of("type", "empty"));
        }
        return responses.getLast();
    }
}
