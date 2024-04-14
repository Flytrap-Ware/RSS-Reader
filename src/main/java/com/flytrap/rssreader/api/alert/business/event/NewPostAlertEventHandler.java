package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.alert.business.service.AlertService;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.folder.business.service.FolderReadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewPostAlertEventHandler {

    private final AlertService alertService;
    private final FolderReadService folderReadService;

    @Async
    @EventListener(NewPostAlertEventHolder.class)
    public void handle(NewPostAlertEventHolder event) {
        NewPostAlertEventParam param = event.value();

        List<Alert> alerts = alertService.getAlertListBySubscribe(param.subscribe().getId());
        if (!alerts.isEmpty()) {
            alerts.forEach(alert ->
                alertService.sendAlertToPlatform(
                    folderReadService.findById(alert.getFolderId()).getName(),
                    alert.getWebhookUrl(),
                    param.posts()
                ));
        }
    }
}

