package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.external.AlertSender;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertEntityJpaRepository alertRepository;
    private final List<AlertSender> alertSenders;

    public Alert registerAlert(Long folderId, Long memberId, String webhookUrl) {
        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(webhookUrl);

        return alertRepository.save(
                        AlertEntity.create(memberId, folderId, alertPlatform, webhookUrl))
                .toDomain();
    }

    public void removeAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new NoSuchDomainException(Alert.class))
                .toDomain();

        alertRepository.deleteById(alert.getId());
    }

    public void sendAlertToPlatform(String folderName, String webhookUrl, List<PostEntity> posts) {
        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(webhookUrl);

        for (AlertSender alertSender : alertSenders) {
            if (alertSender.isSupport(alertPlatform)) {
                alertSender.sendAlert(folderName, webhookUrl, posts);
            }
        }
    }

    public List<Alert> getAlertListBySubscribe(Long subscribeId) {
        return alertRepository.findAlertsBySubscribeId(subscribeId)
                .stream()
                .map(AlertEntity::toDomain)
                .toList();
    }

    public List<Alert> getAlertListByFolder(Long folderId) {
        return alertRepository.findAllByFolderId(folderId)
                .stream()
                .map(AlertEntity::toDomain)
                .toList();
    }

}

