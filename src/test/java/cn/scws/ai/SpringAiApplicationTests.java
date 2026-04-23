package cn.scws.ai;

import cn.scws.ai.tencent.support.TencentChatProperties;
import cn.scws.ai.tencent.v1.TencentV1ChatOptions;
import cn.scws.ai.tencent.v1.TencentV1EventEnums;
import cn.scws.ai.tencent.v2.TencentV2ChatOptions;
import cn.scws.ai.tencent.v2.TencentV2EventEnums;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
class SpringAiApplicationTests {
    @Resource
    private ChatClient.Builder chatClientBuilder;

    @Resource
    private TencentChatProperties tencentChatProperties;

    @Test
    void callV1TencentChat() {
        ChatClient chatClient = chatClientBuilder.build();
        String userInput = "你好，请问你是谁？";

        System.out.println("============================================");
        System.out.println("用户提问: " + userInput);
        System.out.println("============================================\n");

        chatClient.prompt()
                .user(userInput)
                .options(TencentV1ChatOptions.builder()
                        .requestId(UUID.randomUUID().toString())
                        .sessionId(UUID.randomUUID().toString())
                        .botAppKey(tencentChatProperties.getAppKey())
                        .build())
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    var output = response.getResult().getOutput();
                    String content = output.getText();
                    Map<String, Object> metadata = output.getMetadata();
                    String eventType = (String) metadata.get("type");
                    if (TencentV1EventEnums.Reply.getType().equals(eventType)) {
                        System.out.print(content);
                    } else {
                        System.out.print("\n>>> " + eventType + ":\n");
                        System.out.print(metadata);
                    }
                })
                .doOnError(e -> System.err.println("\n系统异常: " + e.getMessage()))
                .doOnComplete(() -> System.out.println("\n\n============================================"))
                .blockLast();
    }

    @Test
    void callV2TencentChat() {
        ChatClient chatClient = chatClientBuilder.build();
        String userInput = "你好，请问你是谁？";

        System.out.println("============================================");
        System.out.println("用户提问: " + userInput);
        System.out.println("============================================\n");

        chatClient.prompt()
                .user(userInput)
                .options(TencentV2ChatOptions.builder()
                        .requestId(UUID.randomUUID().toString())
                        .conversationId(UUID.randomUUID().toString())
                        .appKey(tencentChatProperties.getAppKey())
                        .visitorId("92040404798447737")
                        .build())
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    var output = response.getResult().getOutput();
                    String content = output.getText();
                    Map<String, Object> metadata = output.getMetadata();
                    String eventType = (String) metadata.get("type");
                    if (TencentV2EventEnums.ResponseCompleted.getType().equals(eventType)) {
                        System.out.print(content);
                    } else {
                        System.out.print("\n>>> " + eventType + ":\n");
                        System.out.print(metadata);
                    }
                })
                .doOnError(e -> System.err.println("\n系统异常: " + e.getMessage()))
                .doOnComplete(() -> System.out.println("\n\n============================================"))
                .blockLast();
    }
}
