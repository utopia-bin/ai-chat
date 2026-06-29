package cn.scws.ai.chat.tencent.v1;

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
 * V1 AutoConfiguration Of Tencent Knowledge Engine
 * <br/>
 * <a href="https://cloud.tencent.com/document/product/1759/105561">参考文档</a>
 *
 * @author Bin
 * @version 1.0
 * @date 2026/4/2 11:11
 * @since 1.0
 */
@ConditionalOnClass({TencentV1Api.class})
@EnableConfigurationProperties({TencentChatProperties.class})
@ConditionalOnProperty(
        name = {"spring.ai.model.chat"},
        havingValue = "tencent-v1"
)
@Configuration(proxyBeanMethods = false)
public class TencentV1AutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(TencentV1Api.class)
    public TencentV1Api tencentV1Api(TencentChatProperties properties, WebClient.Builder webClientBuilder) {
        return new TencentV1Api(
                properties.getBaseUrl(),
                TencentChatProperties.CHAT_PATH_V1,
                webClientBuilder,
                new DefaultResponseErrorHandler()
        );
    }

    @Bean
    @ConditionalOnMissingBean(ChatModel.class)
    public TencentV1ChatModel tencentV1ChatModel(TencentV1Api tencentV1Api, TencentChatProperties properties) {
        return new TencentV1ChatModel(tencentV1Api, properties.resolveV1Options());
    }
}
