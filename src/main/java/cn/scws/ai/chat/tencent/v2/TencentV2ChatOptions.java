package cn.scws.ai.chat.tencent.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.ai.chat.prompt.ChatOptions;

import java.util.List;

/**
 * V2 Options Of Tencent Knowledge Engine
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/1759/129202">参考文档</a>
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
public class TencentV2ChatOptions implements ChatOptions {
    @JsonProperty("RequestId")
    private String requestId;
    @JsonProperty("ConversationId")
    private String conversationId;
    @JsonProperty("AppKey")
    private String appKey;
    @JsonProperty("VisitorId")
    private String visitorId;
    @JsonProperty("StreamingThrottle")
    private Integer streamingThrottle;
    @JsonProperty("Incremental")
    private Boolean incremental;
    @JsonProperty("SearchNetwork")
    private String searchNetwork;
    @JsonProperty("ModelName")
    private String modelName;
    @JsonProperty("Stream")
    private String stream;
    @JsonProperty("WorkflowStatus")
    private String workflowStatus;
    @JsonProperty("EnableMultiIntent")
    private Boolean enableMultiIntent;
    @JsonProperty("GenerateAgain")
    private Boolean generateAgain;

    @Override
    public String getModel() {
        return this.modelName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ChatOptions> T copy() {
        return (T) TencentV2ChatOptions.builder()
                .requestId(this.requestId)
                .conversationId(this.conversationId)
                .appKey(this.appKey)
                .visitorId(this.visitorId)
                .streamingThrottle(this.streamingThrottle)
                .incremental(this.incremental)
                .searchNetwork(this.searchNetwork)
                .modelName(this.modelName)
                .stream(this.stream)
                .workflowStatus(this.workflowStatus)
                .enableMultiIntent(this.enableMultiIntent)
                .generateAgain(this.generateAgain)
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
