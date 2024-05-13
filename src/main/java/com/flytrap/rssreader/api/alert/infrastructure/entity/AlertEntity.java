package com.flytrap.rssreader.api.alert.infrastructure.entity;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertCreate;
import com.flytrap.rssreader.api.alert.domain.AlertId;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(name = "alert")
public class AlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Enumerated(EnumType.STRING)
    private AlertPlatform alertPlatform;

    @Column(name = "webhook_url", length = 2500, nullable = false)
    private String webhookUrl;

    @Builder
    protected AlertEntity(Long id, Long memberId, Long folderId, AlertPlatform alertPlatform,
        String webhookUrl) {
        this.id = id;
        this.memberId = memberId;
        this.folderId = folderId;
        this.alertPlatform = alertPlatform;
        this.webhookUrl = webhookUrl;
    }

    public static AlertEntity from(AlertCreate alertCreate) {
        return AlertEntity.builder()
            .memberId(alertCreate.accountId().value())
            .folderId(alertCreate.folderId().value())
            .alertPlatform(alertCreate.alertPlatform())
            .webhookUrl(alertCreate.webhookUrl())
            .build();
    }

    public Alert toReadOnly() {
        return Alert.builder()
            .id(new AlertId(id))
            .memberId(memberId)
            .folderId(folderId)
            .alertPlatform(alertPlatform)
            .webhookUrl(webhookUrl)
            .build();
    }
}
