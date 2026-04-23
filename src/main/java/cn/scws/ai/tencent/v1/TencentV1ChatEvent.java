package cn.scws.ai.tencent.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * V1 Chat Event Of Tencent Knowledge Engine
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/1759/105561">参考文档</a>
 *
 * @author Bin
 * @version 1.0
 * @date 2026/4/1 17:05
 * @since 1.0
 */
public class TencentV1ChatEvent {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Reply(
            @JsonProperty("request_id") String requestId,
            @JsonProperty("content") String content,
            @JsonProperty("record_id") String recordId,
            @JsonProperty("related_record_id") String relatedRecordId,
            @JsonProperty("session_id") String sessionId,
            @JsonProperty("is_from_self") Boolean isFromSelf,
            @JsonProperty("can_rating") String canRating,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("is_final") Boolean isFinal,
            @JsonProperty("is_evil") Boolean isEvil,
            @JsonProperty("is_llm_generated") Boolean isLlmGenerated,
            @JsonProperty("reply_method") Integer replyMethod,
            @JsonProperty("knowledge") List<TencentV1Api.HitKnowledge> knowledge,
            @JsonProperty("quote_infos ") List<TencentV1Api.QuoteInfos> quoteInfos
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TokenStat(
            @JsonProperty("session_id") String sessionId,
            @JsonProperty("request_id") String requestId,
            @JsonProperty("record_id") String recordId,
            @JsonProperty("status_summary") String statusSummary,
            @JsonProperty("status_summary_title") String statusSummaryTitle,
            @JsonProperty("elapsed") Integer elapsed,
            @JsonProperty("token_count") Integer tokenCount,
            @JsonProperty("procedures") List<TencentV1Api.TokenStatProcedure> procedures
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Reference(
            @JsonProperty("record_id") String recordId,
            @JsonProperty("references") List<TencentV1Api.ReferenceItem> references
    ) {

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Error(
            @JsonProperty("code") Integer code,
            @JsonProperty("message") String message
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Thought(
            @JsonProperty("elapsed") Integer elapsed,
            @JsonProperty("is_workflow") Boolean isWorkflow,
            @JsonProperty("procedures") List<TencentV1Api.ThoughtProcedure> procedures,
            @JsonProperty("session_id") String sessionId,
            @JsonProperty("request_id") String requestId,
            @JsonProperty("record_id") String recordId,
            @JsonProperty("trace_id") String traceId,
            @JsonProperty("workflow_name") String workflowName
    ) {
    }
}
