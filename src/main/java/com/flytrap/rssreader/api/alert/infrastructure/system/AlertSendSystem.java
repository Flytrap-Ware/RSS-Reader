package com.flytrap.rssreader.api.alert.infrastructure.system;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.external.AlertSender;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertSendSystem {

    private final AlertEntityJpaRepository alertRepository;
    private final List<AlertSender> alertSenders;

    public void sendAlertToPlatform(String folderName, String webhookUrl, List<PostEntity> posts) {
        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(webhookUrl);

        for (AlertSender alertSender : alertSenders) {
            if (alertSender.isSupport(alertPlatform)) {
                alertSender.sendAlert(folderName, webhookUrl, posts);
            }
        }
    }

    public List<Alert> getAlertListByRssSource(Long rssSourceId) {
        return alertRepository.findAlertsByRssSourceId(rssSourceId)
            .stream()
            .map(AlertEntity::toReadOnly)
            .toList();
    }
}
