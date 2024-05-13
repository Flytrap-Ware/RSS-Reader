package com.flytrap.rssreader.api.alert.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Domain(name = "alert")
public class Alert  implements DefaultDomain {

    private final AlertId id;
    private final Long memberId;
    private final Long folderId;
    private final AlertPlatform alertPlatform;
    private final String webhookUrl;

    @Builder
    protected Alert(AlertId id, Long memberId, Long folderId, AlertPlatform alertPlatform, String webhookUrl) {
        this.id = id;
        this.memberId = memberId;
        this.folderId = folderId;
        this.alertPlatform = alertPlatform;
        this.webhookUrl = webhookUrl;
    }
}
