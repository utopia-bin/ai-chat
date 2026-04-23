package cn.scws.ai.tencent.support;

import cn.scws.ai.tencent.v1.TencentV1ChatOptions;
import cn.scws.ai.tencent.v2.TencentV2ChatOptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Properties Of Tencent Knowledge Engine
 *
 * @author Bin
 * @version 1.0
 * @date 2026/4/2 10:59
 * @since 1.0
 */
@Setter
@Getter
@SuppressWarnings("unused")
@ConfigurationProperties(prefix = "spring.ai.tencent.chat")
public class TencentChatProperties {
    public static final String CONFIG_PREFIX = "spring.ai.tencent.chat";
    public static final String DEFAULT_BASE_URL = "https://tc-adp.scws.cn";
    public static final String CHAT_PATH_V1 = "/v1/qbot/chat/sse";
    public static final String CHAT_PATH_V2 = "/adp/v2/chat";

    private String baseUrl = DEFAULT_BASE_URL;
    private String appKey;

    @NestedConfigurationProperty
    private TencentV1ChatOptions v1Options = TencentV1ChatOptions.builder().build();

    @NestedConfigurationProperty
    private TencentV2ChatOptions v2Options = TencentV2ChatOptions.builder().build();

    public TencentV1ChatOptions resolveV1Options() {
        TencentV1ChatOptions options = this.v1Options.copy();
        if (options.getBotAppKey() == null || options.getBotAppKey().isBlank()) {
            options.setBotAppKey(this.appKey);
        }
        return options;
    }

    public TencentV2ChatOptions resolveV2Options() {
        TencentV2ChatOptions options = this.v2Options.copy();
        if (options.getAppKey() == null || options.getAppKey().isBlank()) {
            options.setAppKey(this.appKey);
        }
        return options;
    }
}
