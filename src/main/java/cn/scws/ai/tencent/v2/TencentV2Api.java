package cn.scws.ai.tencent.v2;

import cn.scws.ai.tencent.support.AbstractTencentStreamingApi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * V2 Api Of Tencent Knowledge Engine
 */
@Getter
public class TencentV2Api extends AbstractTencentStreamingApi {
    private final String baseUrl;

    private final String chatPath;

    public TencentV2Api(String baseUrl, String chatPath, WebClient.Builder webClientBuilder,
                        ResponseErrorHandler responseErrorHandler) {
        super(baseUrl, webClientBuilder, responseErrorHandler);
        this.baseUrl = baseUrl;
        this.chatPath = chatPath;
    }

    public Flux<ChatResponseChunk> chatCompletionStream(ChatRequest request) {
        Assert.notNull(request, "request cannot be null");
        Assert.hasText(request.appKey(), "app key cannot be empty");
        return this.postForStream(this.chatPath, request.requestId(), request, ChatResponseChunk.class);
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatRequest(
            @JsonProperty("RequestId") String requestId,
            @JsonProperty("ConversationId") String conversationId,
            @JsonProperty("AppKey") String appKey,
            @JsonProperty("VisitorId") String visitorId,
            @JsonProperty("Contents") List<TencentV2Api.Content> contents,
            @JsonProperty("StreamingThrottle") Integer streamingThrottle,
            @JsonProperty("SystemRole") String systemRole,
            @JsonProperty("Incremental") Boolean incremental,
            @JsonProperty("SearchNetwork") String searchNetwork,
            @JsonProperty("ModelName") String modelName,
            @JsonProperty("Stream") String stream,
            @JsonProperty("WorkflowStatus") String workflowStatus,
            @JsonProperty("EnableMultiIntent") Boolean enableMultiIntent,
            @JsonProperty("GenerateAgain") Boolean generateAgain
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatResponseChunk(
            @JsonProperty("event") String event,
            @JsonProperty("data") Map<String, Object> data
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Record(
            @JsonProperty("Role") String role,
            @JsonProperty("RecordId") String recordId,
            @JsonProperty("RelatedRecordId") String relatedRecordId,
            @JsonProperty("ConversationId") String conversationId,
            @JsonProperty("Status") String status,
            @JsonProperty("StatusDesc") String statusDesc,
            @JsonProperty("Messages") List<Message> messages,
            @JsonProperty("Procedures") List<Procedure> procedures,
            @JsonProperty("StatInfo") StatInfo statInfo,
            @JsonProperty("ExtraInfo") RecordExtraInfo extraInfo
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Message(
            @JsonProperty("Type") String type,
            @JsonProperty("MessageId") String messageId,
            @JsonProperty("Name") String name,
            @JsonProperty("Title") String title,
            @JsonProperty("Icon") String icon,
            @JsonProperty("Status") String status,
            @JsonProperty("StatusDesc") String statusDesc,
            @JsonProperty("Contents") List<Content> contents,
            @JsonProperty("ExtraInfo") MessageExtraInfo extraInfo
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Procedure(
            @JsonProperty("Name") String name,
            @JsonProperty("Title") String title,
            @JsonProperty("Status") String status,
            @JsonProperty("IntentCate") String intentCate,
            @JsonProperty("ResourceStatus") Integer resourceStatus,
            @JsonProperty("Type") String type,
            @JsonProperty("Knowledge") Knowledge knowledge,
            @JsonProperty("Workflow") WorkflowProcedure workflow,
            @JsonProperty("Agent") Agent agent,
            @JsonProperty("StatInfos") List<StatInfo> statInfos
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record StatInfo(
            @JsonProperty("Elapsed") Integer elapsed,
            @JsonProperty("StartTime") Long startTime,
            @JsonProperty("InputTokens") Integer inputTokens,
            @JsonProperty("OutputTokens") Integer outputTokens,
            @JsonProperty("TotalTokens") Integer totalTokens,
            @JsonProperty("ModelName") String modelName,
            @JsonProperty("FirstTokenCost") Integer firstTokenCost,
            @JsonProperty("TotalCost") Integer totalCost
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record RecordExtraInfo(
            @JsonProperty("RequestId") String requestId,
            @JsonProperty("TraceId") String traceId,
            @JsonProperty("Elapsed") Integer elapsed,
            @JsonProperty("StartTime") Long startTime,
            @JsonProperty("IsFromSelf") Boolean isFromSelf,
            @JsonProperty("IsLlmGenerated") Boolean isLlmGenerated,
            @JsonProperty("CanRating") Boolean canRating,
            @JsonProperty("CanFeedback") Boolean canFeedback,
            @JsonProperty("ReplyMethod") Integer replyMethod
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Content(
            @JsonProperty("Type") String type,
            @JsonProperty("Text") String text,
            @JsonProperty("Image") Image image,
            @JsonProperty("File") FileInfo file,
            @JsonProperty("CustomVariables") Map<String, String> customVariables,
            @JsonProperty("WidgetAction") WidgetAction widgetAction,
            @JsonProperty("QuoteInfos") List<QuoteInfo> quoteInfos,
            @JsonProperty("References") List<Reference> references,
            @JsonProperty("OptionCards") List<String> optionCards,
            @JsonProperty("CustomParams") List<String> customParams,
            @JsonProperty("Sandbox") Sandbox sandbox,
            @JsonProperty("WebSearch") WebSearch webSearch,
            @JsonProperty("FileCollection") FileCollection fileCollection,
            @JsonProperty("RelatedRecordId") String relatedRecordId,
            @JsonProperty("Widget") Widget widget
    ) {
        public Content(String text) {
            this("text", text, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        public Content(Image image) {
            this("image", null, image, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        public Content(FileInfo file) {
            this("file", null, null, file, null, null, null, null, null, null, null, null, null, null, null);
        }

        public Content(Map<String, String> customVariables) {
            this("custom_variables", null, null, null, customVariables, null, null, null, null, null, null, null, null, null, null);
        }

        public Content(WidgetAction widgetAction) {
            this("widget_action", null, null, null, null, widgetAction, null, null, null, null, null, null, null, null, null);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MessageExtraInfo(
            @JsonProperty("Elapsed") Integer elapsed,
            @JsonProperty("StartTime") Long startTime,
            @JsonProperty("AgentName") String agentName,
            @JsonProperty("AgentIcon") String agentIcon,
            @JsonProperty("ParentMessageId") String parentMessageId
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Knowledge(
            @JsonProperty("Content") String content,
            @JsonProperty("System") String system,
            @JsonProperty("RewriteQuery") String rewriteQuery,
            @JsonProperty("CustomVariables") List<String> customVariables,
            @JsonProperty("Histories") List<History> histories,
            @JsonProperty("Outputs") List<KnowledgeOutput> outputs
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record WorkflowProcedure(
            @JsonProperty("WorkflowId") String workflowId,
            @JsonProperty("WorkflowName") String workflowName,
            @JsonProperty("WorkflowReleaseTime") String workflowReleaseTime,
            @JsonProperty("WorkflowRunId") String workflowRunId,
            @JsonProperty("Content") String content,
            @JsonProperty("Outputs") List<String> outputs,
            @JsonProperty("OptionCardIndex") OptionCardIndex optionCardIndex,
            @JsonProperty("RunNodes") List<RunNode> runNodes
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Agent(
            @JsonProperty("Input") String input,
            @JsonProperty("Output") String output,
            @JsonProperty("ModelName") String modelName,
            @JsonProperty("Content") String content,
            @JsonProperty("System") String system,
            @JsonProperty("RewriteQuery") String rewriteQuery,
            @JsonProperty("CustomVariables") List<String> customVariables
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Error(
            @JsonProperty("Code") String code,
            @JsonProperty("Message") String message,
            @JsonProperty("RequestId") String requestId,
            @JsonProperty("TraceId") String traceId,
            @JsonProperty("Elapsed") Integer elapsed,
            @JsonProperty("StartTime") Long startTime
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record RunNode(
            @JsonProperty("Elapsed") Integer elapsed,
            @JsonProperty("NodeId") String nodeId,
            @JsonProperty("NodeName") String nodeName,
            @JsonProperty("NodeType") Integer nodeType,
            @JsonProperty("Status") Integer status,
            @JsonProperty("Input") String input,
            @JsonProperty("InputRef") String inputRef,
            @JsonProperty("Output") String output,
            @JsonProperty("OutputRef") String outputRef,
            @JsonProperty("TaskOutput") String taskOutput,
            @JsonProperty("TaskOutputRef") String taskOutputRef,
            @JsonProperty("Reply") String reply,
            @JsonProperty("FailCode") String failCode,
            @JsonProperty("FailMessage") String failMessage,
            @JsonProperty("BelongNodeId") String belongNodeId,
            @JsonProperty("IsCurrent") Boolean isCurrent,
            @JsonProperty("StatInfos") List<StatInfo> statInfos
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record OptionCardIndex(
            @JsonProperty("RecordId") String recordId,
            @JsonProperty("Index") Integer index
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record KnowledgeOutput(
            @JsonProperty("Type") Integer type,
            @JsonProperty("Content") String content
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record History(
            @JsonProperty("Assistant") String assistant,
            @JsonProperty("User") String user
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FileCollection(
            @JsonProperty("MaxFileCount") Integer maxFileCount,
            @JsonProperty("SupportedFileTypes") List<String> supportedFileTypes
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record WebSearch(
            @JsonProperty("Content") String content
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Sandbox(
            @JsonProperty("Url") String url,
            @JsonProperty("DisplayUrl") String displayUrl,
            @JsonProperty("Content") String content
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record WebSearchRefer(
            @JsonProperty("Url") String url
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record QaRefer(
            @JsonProperty("ReferenceId") String referenceId,
            @JsonProperty("QaId") String qaId,
            @JsonProperty("KnowledgeId") String knowledgeId,
            @JsonProperty("KnowledgeName") String knowledgeName
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record DocRefer(
            @JsonProperty("ReferenceId") String referenceId,
            @JsonProperty("DocId") String docId,
            @JsonProperty("DocName") String docName,
            @JsonProperty("KnowledgeId") String knowledgeId,
            @JsonProperty("KnowledgeName") String knowledgeName,
            @JsonProperty("Url") String url
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record GraphRAGRefer(
            @JsonProperty("ReferenceId") String referenceId,
            @JsonProperty("KnowledgeId") String knowledgeId,
            @JsonProperty("KnowledgeName") String knowledgeName
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Reference(
            @JsonProperty("Index") Integer index,
            @JsonProperty("Type") Integer type,
            @JsonProperty("Name") String name,
            @JsonProperty("DocRefer") DocRefer docRefer,
            @JsonProperty("QaRefer") QaRefer qaRefer,
            @JsonProperty("WebSearchRefer") WebSearchRefer webSearchRefer,
            @JsonProperty("GraphRAGRefer") GraphRAGRefer graphRagRefer
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record QuoteInfo(
            @JsonProperty("Position") Integer position,
            @JsonProperty("Index") Integer index
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record WidgetAction(
            @JsonProperty("WidgetId") String widgetId,
            @JsonProperty("WidgetRunId") String widgetRunId,
            @JsonProperty("ActionType") String actionType,
            @JsonProperty("Payload") String payload
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Widget(
            @JsonProperty("WidgetId") String widgetId,
            @JsonProperty("WidgetRunId") String widgetRunId,
            @JsonProperty("State") String state,
            @JsonProperty("Position") Integer position,
            @JsonProperty("View") String view
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FileInfo(
            @JsonProperty("FileName") String fileName,
            @JsonProperty("FileSize") String fileSize,
            @JsonProperty("FileUrl") String fileUrl,
            @JsonProperty("FileType") String fileType,
            @JsonProperty("DocId") String docId
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Image(
            @JsonProperty("Url") String url
    ) {
    }
}
