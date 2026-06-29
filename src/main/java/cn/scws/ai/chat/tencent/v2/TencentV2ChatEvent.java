package cn.scws.ai.chat.tencent.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * V2 Chat Event Of Tencent Knowledge Engine
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/1759/129202">参考文档</a>
 *
 * @author Bin
 * @version 1.0
 * @date 2026/4/1 17:05
 * @since 1.0
 */
public class TencentV2ChatEvent {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record RequestAck(
            @JsonProperty("Type") String type,
            @JsonProperty("RequestAck") TencentV2Api.Record record
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ResponseCreated(
            @JsonProperty("Type") String type,
            @JsonProperty("Response") TencentV2Api.Record record
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ResponseProcessing(
            @JsonProperty("Type") String type,
            @JsonProperty("Response") TencentV2Api.Record record
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ResponseCompleted(
            @JsonProperty("Type") String type,
            @JsonProperty("Response") TencentV2Api.Record record
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MessageAdded(
            @JsonProperty("Type") String type,
            @JsonProperty("Message") TencentV2Api.Message message
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MessageProcessing(
            @JsonProperty("Type") String type,
            @JsonProperty("MessageId") String messageId,
            @JsonProperty("Message") TencentV2Api.Message message
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MessageDone(
            @JsonProperty("Type") String type,
            @JsonProperty("MessageId") String messageId,
            @JsonProperty("Message") TencentV2Api.Message message
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ContentAdded(
            @JsonProperty("Type") String type,
            @JsonProperty("MessageId") String messageId,
            @JsonProperty("ContentIndex") Integer contentIndex,
            @JsonProperty("Content") TencentV2Api.Content content
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TextDelta(
            @JsonProperty("Type") String type,
            @JsonProperty("MessageId") String messageId,
            @JsonProperty("ContentIndex") Integer contentIndex,
            @JsonProperty("Text") String text
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TextReplace(
            @JsonProperty("Type") String type,
            @JsonProperty("MessageId") String messageId,
            @JsonProperty("ContentIndex") Integer contentIndex,
            @JsonProperty("Text") String text
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record QuoteInfoAdded(
            @JsonProperty("Type") String type,
            @JsonProperty("MessageId") String messageId,
            @JsonProperty("ContentIndex") Integer contentIndex,
            @JsonProperty("QuoteInfo") TencentV2Api.QuoteInfo quoteInfo
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ReferenceAdded(
            @JsonProperty("Type") String type,
            @JsonProperty("MessageId") String messageId,
            @JsonProperty("ContentIndex") Integer contentIndex,
            @JsonProperty("Reference") TencentV2Api.Reference reference
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Error(
            @JsonProperty("Type") String type,
            @JsonProperty("Error") TencentV2Api.Error reference
    ) {
    }
}
