package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.infrastructure.system.AlertSendSystem;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
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
    @EventListener(NewPostAlertEvent.class)
    public void handle(NewPostAlertEvent event) {
        List<Alert> alerts = alertSendSystem.getAlertListByRssSource(event.rssSource().getId());
        if (!alerts.isEmpty()) {
            alerts.forEach(alert ->
                alertSendSystem.sendAlertToPlatform(
                    folderQuery.read(alert.getFolderId()).getName(),
                    alert.getWebhookUrl(),
                    event.posts()
                ));
        }
    }
}
