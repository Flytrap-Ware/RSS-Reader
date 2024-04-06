package com.flytrap.rssreader.api.alert.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Domain(name = "alert")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alert  implements DefaultDomain {

    private Long id;
    private Long memberId;
    private Long folderId;
    private AlertPlatform alertPlatform;
    private String webhookUrl;

    @Builder
    protected Alert(Long id, Long memberId, Long folderId, AlertPlatform alertPlatform, String webhookUrl) {
        this.id = id;
        this.memberId = memberId;
        this.folderId = folderId;
        this.alertPlatform = alertPlatform;
        this.webhookUrl = webhookUrl;
    }
}
