package cn.scws.ai.chat.tencent.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * V2 Event Enums Of Tencent Knowledge Engine
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/1759/129202">参考文档</a>
 *
 * @author Bin
 * @version 1.0
 * @date 2026/4/3 11:00
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum TencentV2EventEnums {
    RequestAck("request.ack", "请求确认事件"),
    ResponseCreated("response.created", "响应创建事件"),
    ResponseProcessing("response.processing", "响应处理中事件"),
    ResponseCompleted("response.completed", "响应完成事件"),
    MessageAdded("message.added", "消息增加事件"),
    MessageProcessing("message.processing", "消息处理中事件"),
    MessageDone("message.done", "消息完成事件"),
    ContentAdded("content.added", "内容增加事件"),
    TextDelta("text.delta", "文本内容增量事件"),
    TextReplace("text.replace", "文本内容替换事件"),
    QuoteInfoAdded("quote_info.added", "角标增加事件"),
    ReferenceAdded("reference.added", "引文增加事件"),
    Error("error", "错误事件");


    private final String type;
    private final String desc;

    public static TencentV2EventEnums getByType(String type) {
        for (TencentV2EventEnums value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return Error;
    }
}
