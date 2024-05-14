package com.flytrap.rssreader.api.alert.presentation.dto;

import com.flytrap.rssreader.api.alert.domain.Alert;

public record AlertResponse(
    Long id,
    String platform,
    String webhookUrl
) {

    public static AlertResponse from(Alert alert) {
        return new AlertResponse(
            alert.getId().value(),
            alert.getAlertPlatform().name(),
            alert.getWebhookUrl());
    }
}
