package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.infrastructure.system.AlertSendSystem;
import com.flytrap.rssreader.api.folder.business.service.FolderReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewPostAlertEventListener {

    private final AlertSendSystem alertSendSystem;
    private final FolderReadService folderReadService;

    @Async
    @EventListener(NewPostAlertEvent.class)
    public void handle(NewPostAlertEvent event) {
        List<Alert> alerts = alertSendSystem.getAlertListBySubscribe(event.subscribe().getId());
        if (!alerts.isEmpty()) {
            alerts.forEach(alert ->
                alertSendSystem.sendAlertToPlatform(
                    folderReadService.findById(alert.getFolderId()).getName(),
                    alert.getWebhookUrl(),
                    event.posts()
                ));
        }
    }
}
