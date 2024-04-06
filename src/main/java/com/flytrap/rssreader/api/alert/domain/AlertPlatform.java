package com.flytrap.rssreader.api.alert.domain;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertPlatform {

    SLACK("https://hooks.slack.com/services/"),
    DISCORD("https://discord.com/api/webhooks/");

    private final String signatureUrl;

    public static AlertPlatform parseWebhookUrl(String webhookUrl) {

        return Arrays.stream(AlertPlatform.values())
            .filter(alertPlatform -> webhookUrl.contains(alertPlatform.getSignatureUrl()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("잘못된 웹 훅 URL 입니다."));
    }
}
