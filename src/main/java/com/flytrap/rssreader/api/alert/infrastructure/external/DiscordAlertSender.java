package com.flytrap.rssreader.api.alert.infrastructure.external;

import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.business.service.dto.AlertParam;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordAlertSender implements AlertSender {

    private static final int MAX_CONTENT_LENGTH = 2000;

    private final WebClient.Builder webClient;

    @Override
    public boolean isSupport(AlertPlatform alertPlatform) {

        return alertPlatform == AlertPlatform.DISCORD;
    }

    @Override
    public void sendAlert(AlertParam value) {
        String content = generateContent(value);

        webClient
            .baseUrl(value.webhookUrl())
            .build()
            .method(HttpMethod.POST)
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(Map.of("content", content)))
            .retrieve()
            .bodyToMono(Void.class)
            .doOnSuccess(response -> log.info("Discord notification sent successfully. Response: {}", response))
            .doOnError(error -> log.error("Error sending Discord notification. Error: {}", error.getLocalizedMessage()))
            .block();
    }

    /**
     * Discord에 알림 메시지를 보낼 때는 Request Message Body에 Json 포맷으로 content 필드에 메시지를 넣어서 보냅니다.
     * 메시지 길이는 2000자 까지 이며, 2000자가 넘어가면 400 Bad Request 응답을 반환 합니다.
     *
     * 참고: Discord API Docs - Create Message
     * https://discord.com/developers/docs/resources/channel#create-message
     *
     * @return Discord 웹 훅으로 보낼 메시지
     */
    public String generateContent(AlertParam value) {
        StringBuilder builder = new StringBuilder();
        builder.append("*새로운 글이 갱신되었습니다!*\n\n");
        builder.append("*폴더 이름:* ").append(value.folderName()).append("\n\n");

        for (Entry<String, String> entry : value.posts().entrySet()) {
            String appendStr = "[" + entry.getValue() + "](" + entry.getKey() + ")\n\n";

            if (builder.length() + appendStr.length() > MAX_CONTENT_LENGTH) {
                break;
            }

            builder.append(appendStr);
        }

        return builder.toString();
    }
}
