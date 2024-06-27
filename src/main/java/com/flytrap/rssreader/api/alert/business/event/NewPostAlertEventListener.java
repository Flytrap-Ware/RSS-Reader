package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.infrastructure.system.AlertSendSystem;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewPostAlertEventListener {

    private final AlertSendSystem alertSendSystem;
    private final FolderQuery folderQuery;

    @Async
    @EventListener(NewPostAlertEvent1.class)
    public void handle(NewPostAlertEvent1 event) {
        List<Alert> alerts = alertSendSystem.getAlertListByRssSource(event.rssSource().getId());
        if (!alerts.isEmpty()) {
            alerts.forEach(alert ->
                alertSendSystem.sendAlertToPlatform(
                    folderQuery.read(alert.getFolderId())
                        .orElseThrow(() -> new NoSuchDomainException(Folder.class))
                        .getName(),
                    alert.getWebhookUrl(),
                    event.posts()
                ));
        }
    }
}
