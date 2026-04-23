package cn.scws.ai.tencent.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * V1 Event Enums Of Tencent Knowledge Engine
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/1759/105561">参考文档</a>
 *
 * @author Bin
 * @version 1.0
 * @date 2026/4/3 11:00
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum TencentV1EventEnums {
    Reply("reply", "回复"),
    Reference("reference", "引用"),
    Thought("thought", "思考"),
    TokenStat("token_stat", "词元统计"),
    Error("error", "错误");

    private final String type;
    private final String desc;

    public static TencentV1EventEnums getByType(String type) {
        for (TencentV1EventEnums value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return Error;
    }
}
