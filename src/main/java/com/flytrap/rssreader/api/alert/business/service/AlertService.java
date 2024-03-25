package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.api.alert.business.event.subscribe.AlertEvent;
import com.flytrap.rssreader.api.alert.business.service.dto.AlertParam;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.global.event.PublishEvent;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import com.flytrap.rssreader.api.alert.infrastructure.external.AlertSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @PublishEvent(eventType = AlertEvent.class,
            params = "#{T(com.flytrap.rssreader.api.alert.business.service.dto.AlertParam).create(#folderName, #webhookUrl, #posts)}")
    public void publishAlertEvent(String folderName, String webhookUrl, Map<String, String> posts) {}

    public void sendAlertToPlatform(AlertParam value) {
        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(value.webhookUrl());

        for (AlertSender alertSender : alertSenders) {
            if (alertSender.isSupport(alertPlatform)) {
                alertSender.sendAlert(value);
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

