package cn.scws.ai.chat.tencent.v1;

import cn.scws.ai.chat.tencent.support.AbstractTencentStreamingApi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * V1 Api Of Tencent Knowledge Engine
 */
@Getter
public class TencentV1Api extends AbstractTencentStreamingApi {
    private final String baseUrl;

    private final String chatPath;

    public TencentV1Api(String baseUrl, String chatPath, WebClient.Builder webClientBuilder,
                        ResponseErrorHandler responseErrorHandler) {
        super(baseUrl, webClientBuilder, responseErrorHandler);
        this.baseUrl = baseUrl;
        this.chatPath = chatPath;
    }

    public Flux<ChatResponseChunk> chatCompletionStream(ChatRequest request) {
        Assert.notNull(request, "request cannot be null");
        Assert.hasText(request.botAppKey(), "bot app key cannot be empty");
        return this.postForStream(this.chatPath, request.requestId(), request, ChatResponseChunk.class);
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatRequest(
            @JsonProperty("request_id") String requestId,
            @JsonProperty("session_id") String sessionId,
            @JsonProperty("bot_app_key") String botAppKey,
            @JsonProperty("visitor_biz_id") String visitorBizId,
            @JsonProperty("content") String content,
            @JsonProperty("model_name") String modelName,
            @JsonProperty("system_role") String systemRole,
            @JsonProperty("search_network") String searchNetwork,
            @JsonProperty("streaming_throttle") Integer streamingThrottle,
            @JsonProperty("custom_variables") Map<String, String> customVariables,
            @JsonProperty("workflow_status") String workflowStatus,
            @JsonProperty("stream") String stream
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatResponseChunk(
            @JsonProperty("type") String type,
            @JsonProperty("payload") Map<String, Object> payload,
            @JsonProperty("error") Map<String, Object> error,
            @JsonProperty("request_id") String requestId
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record HitKnowledge(
            @JsonProperty("id") String id,
            @JsonProperty("type") Integer type,
            @JsonProperty("seg_id") String segId
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record QuoteInfos(
            @JsonProperty("index") Integer index,
            @JsonProperty("position") Integer position
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TokenStatProcedure(
            @JsonProperty("name") String name,
            @JsonProperty("title") String title,
            @JsonProperty("status") String status,
            @JsonProperty("input_count") String inputCount,
            @JsonProperty("output_count") String outputCount,
            @JsonProperty("count") String count
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ReferenceItem(
            @JsonProperty("id") String id,
            @JsonProperty("type") Integer type,
            @JsonProperty("url") String url,
            @JsonProperty("name") String name,
            @JsonProperty("doc_id") String docId
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ThoughtProcedure(
            @JsonProperty("debugging") Debugging debugging,
            @JsonProperty("index") Integer index,
            @JsonProperty("name") String name,
            @JsonProperty("title") String title,
            @JsonProperty("status") String status,
            @JsonProperty("icon") String icon,
            @JsonProperty("switch") String switchFlag,
            @JsonProperty("workflow_name") String workflowName,
            @JsonProperty("plugin_type") Integer pluginType,
            @JsonProperty("elapsed") Integer elapsed
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Debugging(
            @JsonProperty("content") String content
    ) {
    }
}
