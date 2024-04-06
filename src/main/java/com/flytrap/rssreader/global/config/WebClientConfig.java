package com.flytrap.rssreader.global.config;

import com.flytrap.rssreader.global.properties.OauthProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final OauthProperties oauthProperties;

    @Bean(name = "githubAuthorizationServer")
    public WebClient githubAuthorizationServerWebClient() {
        return buildWebClient(oauthProperties.github().accessTokenUri());
    }

    @Bean(name = "githubResourceServer")
    public WebClient githubResourceServerWebClient() {
        return buildWebClient(oauthProperties.github().userResourceUri());
    }

    @Bean(name = "githubResourceEmailServer")
    public WebClient githubResourceEmailServerWebClient() {
        return buildWebClient(oauthProperties.github().userResourceEmailUri());
    }

    /**
     * WebClient 요청시 log 출력합니다.
     * @return ExchangeFilterFunction
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.debug("{} Request: {} {}", clientRequest.url(), clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    /**
     * WebClient 반환시 log 출력합니다.
     * @return ExchangeFilterFunction
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
            return Mono.just(clientResponse);
        });
    }

    /**
     * Base url로 요청을 보내는 WebClient를 반환합니다.
     * @param baseUrl
     * @return WebClient
     */
    private WebClient buildWebClient(String baseUrl) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024*1024*50))
            .build();
        exchangeStrategies
            .messageWriters().stream()
            .filter(LoggingCodecSupport.class::isInstance)
            .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));

        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(
                new ReactorClientHttpConnector(
                    HttpClient
                        .create()
                        .option(
                            ChannelOption.CONNECT_TIMEOUT_MILLIS, 120_000)
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(180))
                            .addHandlerLast(new WriteTimeoutHandler(180))
                        )
                )
            )
            .exchangeStrategies(exchangeStrategies)
            .filter(logRequest())
            .filter(logResponse())
            .build();
    }
}
