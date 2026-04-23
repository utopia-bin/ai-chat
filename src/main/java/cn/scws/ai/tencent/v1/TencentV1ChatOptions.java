package cn.scws.ai.tencent.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.ai.chat.prompt.ChatOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * V1 Options Of Tencent Knowledge Engine
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/1759/105561">参考文档</a>
 *
 * @author Bin
 * @version 1.0
 * @date 2026/3/31 17:13
 * @since 1.0
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TencentV1ChatOptions implements ChatOptions {
    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("bot_app_key")
    private String botAppKey;
    @JsonProperty("session_id")
    private String sessionId;
    @JsonProperty("visitor_biz_id")
    private String visitorBizId;
    @JsonProperty("model_name")
    private String modelName;
    @JsonProperty("search_network")
    private String searchNetwork;
    @JsonProperty("custom_variables")
    private Map<String, String> customVariables;
    @JsonProperty("streaming_throttle")
    private Integer streamingThrottle;
    @JsonProperty("workflow_status")
    private String workflowStatus;
    @JsonProperty("stream")
    private String stream;

    @Override
    public String getModel() {
        return this.modelName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ChatOptions> T copy() {
        return (T) TencentV1ChatOptions.builder()
                .requestId(this.requestId)
                .botAppKey(this.botAppKey)
                .sessionId(this.sessionId)
                .visitorBizId(this.visitorBizId)
                .modelName(this.modelName)
                .searchNetwork(this.searchNetwork)
                .customVariables(this.customVariables != null ? new HashMap<>(this.customVariables) : null)
                .streamingThrottle(this.streamingThrottle)
                .workflowStatus(this.workflowStatus)
                .stream(this.stream)
                .build();
    }

    @Override
    public Double getTemperature() {
        return null;
    }

    @Override
    public Double getTopP() {
        return null;
    }

    @Override
    public Integer getTopK() {
        return null;
    }

    @Override
    public Double getFrequencyPenalty() {
        return null;
    }

    @Override
    public Double getPresencePenalty() {
        return null;
    }

    @Override
    public Integer getMaxTokens() {
        return null;
    }

    @Override
    public List<String> getStopSequences() {
        return null;
    }
}
