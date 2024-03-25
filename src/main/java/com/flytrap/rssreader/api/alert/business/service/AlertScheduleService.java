package com.flytrap.rssreader.api.alert.business.service;

import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEvent;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.folder.business.service.FolderReadService;
import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEventQueue;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertScheduleService {

    private final SubscribeEventQueue queue;
    private final AlertService alertService;
    private final FolderReadService folderReadService;

    @Async("alertThreadExecutor")
    @Scheduled(fixedRate = 1000L)
    public void processAlertSchedule() {
        if (queue.isRemaining()) {
            SubscribeEvent event = queue.poll();

            List<Alert> alerts = alertService.getAlertListBySubscribe(event.subscribeId());
            if (!alerts.isEmpty()) {
                alerts.forEach(alert ->
                    alertService.publishAlertEvent(
                        folderReadService.findById(alert.getFolderId()).getName(),
                        alert.getWebhookUrl(),
                        event.posts()));
            }
        }
    }
}
