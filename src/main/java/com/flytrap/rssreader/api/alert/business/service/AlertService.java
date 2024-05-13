package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.external.AlertSender;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertQuery;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.api.folder.domain.FolderDomain;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertEntityJpaRepository alertRepository;
    private final List<AlertSender> alertSenders;

    private final AlertQuery alertQuery;
    private final FolderValidator folderValidator;

    public List<Alert> getAlertsByFolder(FolderId folderId, AccountId accountId) {

        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(FolderDomain.class);

        return alertQuery.readAllByFolder(folderId);
    }

    public Alert registerAlert(Long folderId, Long memberId, String webhookUrl) {
        AlertPlatform alertPlatform = AlertPlatform.parseWebhookUrl(webhookUrl);

        return alertRepository.save(
                        AlertEntity.create(memberId, folderId, alertPlatform, webhookUrl))
                .toReadOnly();
    }

    public void removeAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new NoSuchDomainException(Alert.class))
                .toReadOnly();

        alertRepository.deleteById(alert.getId().value());
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
                .map(AlertEntity::toReadOnly)
                .toList();
    }

}

