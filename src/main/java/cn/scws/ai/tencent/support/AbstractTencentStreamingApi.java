package cn.scws.ai.tencent.support;

import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractTencentStreamingApi {
    public static final String HTTP_USER_AGENT_HEADER = "User-Agent";
    public static final String REQUEST_ID_HEADER = "X-REQUEST-ID";
    public static final String SPRING_AI_USER_AGENT = "spring-ai";

    private static final Predicate<String> SSE_DONE_PREDICATE = "[DONE]"::equals;

    private final WebClient webClient;

    private final ResponseErrorHandler responseErrorHandler;

    protected AbstractTencentStreamingApi(String baseUrl, WebClient.Builder webClientBuilder,
                                          ResponseErrorHandler responseErrorHandler) {
        this.responseErrorHandler = responseErrorHandler;

        Consumer<HttpHeaders> defaultHeaders = headers -> {
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HTTP_USER_AGENT_HEADER, SPRING_AI_USER_AGENT);
        };

        this.webClient = webClientBuilder.clone()
                .baseUrl(baseUrl)
                .defaultHeaders(defaultHeaders)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
    }

    protected <T> Flux<T> postForStream(String path, String requestId, Object request, Class<T> responseType) {
        Assert.hasText(path, "chat path cannot be empty");
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(responseType, "responseType cannot be null");

        return this.webClient.post()
                .uri(path)
                .headers(headers -> {
                    if (requestId != null && !requestId.isBlank()) {
                        headers.add(REQUEST_ID_HEADER, requestId);
                    }
                })
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(String.class)
                .takeUntil(SSE_DONE_PREDICATE)
                .filter(SSE_DONE_PREDICATE.negate())
                .map(content -> ModelOptionsUtils.jsonToObject(content, responseType))
                .filter(Objects::nonNull);
    }

    protected ResponseErrorHandler getResponseErrorHandler() {
        return this.responseErrorHandler;
    }
}
