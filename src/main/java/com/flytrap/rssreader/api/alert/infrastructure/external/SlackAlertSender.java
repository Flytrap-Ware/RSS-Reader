package com.flytrap.rssreader.api.alert.infrastructure.external;

import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import java.util.List;
import java.util.Map;
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
public class SlackAlertSender implements AlertSender {

    private final WebClient.Builder webClient;

    @Override
    public boolean isSupport(AlertPlatform alertPlatform) {

        return alertPlatform == AlertPlatform.SLACK;
    }

    @Override
    public void sendAlert(String folderName, String webhookUrl, List<PostEntity> posts) {
        String text = generateText(folderName, posts);

        webClient
            .baseUrl(webhookUrl)
            .build()
            .method(HttpMethod.POST)
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(Map.of("text", text)))
            .retrieve()
            .bodyToMono(String.class)
            .doOnSuccess(response -> log.info("Slack notification sent successfully. Response: {}", response))
            .doOnError(error -> log.error("Error sending Slack notification. Error: {}", error.getMessage()))
            .block();
    }

    /**
     * Slack에 알림 메시지를 보낼 때는 Request Message Body에 Json 포맷으로 text 필드에 메시지를 넣어서 보냅니다.
     *
     * * Slack API Docs: https://api.slack.com/messaging/webhooks#posting_with_webhooks
     *
     * @return Slack 웹 훅으로 보낼 메시지
     */
    private String generateText(String folderName, List<PostEntity> posts) {
        StringBuilder builder = new StringBuilder();
        builder.append("*새로운 글이 갱신되었습니다!*\n\n");
        builder.append("*폴더 이름:* ").append(folderName).append("\n\n");

        for (PostEntity post :  posts) {
            builder.append("<").append(post.getGuid()).append("|").append(post.getTitle()).append(">\n\n");
        }

        return builder.toString();
    }
}
