package cn.scws.ai.chat.tencent.v2;

import cn.scws.ai.chat.tencent.support.TencentChatProperties;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * V2 AutoConfiguration Of Tencent Knowledge Engine
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/1759/129202">参考文档</a>
 *
 * @author Bin
 * @version 1.0
 * @date 2026/4/2 11:11
 * @since 1.0
 */
@ConditionalOnClass({TencentV2Api.class})
@EnableConfigurationProperties({TencentChatProperties.class})
@ConditionalOnProperty(
        name = {"spring.ai.model.chat"},
        havingValue = "tencent-v2"
)
@Configuration(proxyBeanMethods = false)
public class TencentV2AutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(TencentV2Api.class)
    public TencentV2Api tencentV2Api(TencentChatProperties properties, WebClient.Builder webClientBuilder) {
        return new TencentV2Api(
                properties.getBaseUrl(),
                TencentChatProperties.CHAT_PATH_V2,
                webClientBuilder,
                new DefaultResponseErrorHandler()
        );
    }

    @Bean
    @ConditionalOnMissingBean(ChatModel.class)
    public TencentV2ChatModel tencentV2ChatModel(TencentV2Api tencentV2Api, TencentChatProperties properties) {
        return new TencentV2ChatModel(tencentV2Api, properties.resolveV2Options());
    }
}
