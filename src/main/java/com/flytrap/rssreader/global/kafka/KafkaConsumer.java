package com.flytrap.rssreader.global.kafka;

import com.flytrap.rssreader.api.alert.infrastructure.entity.OutBoxEventEntity;
import com.flytrap.rssreader.api.alert.infrastructure.system.AlertSendSystem;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final AlertSendSystem alertSendSystem;
    private final FolderQuery folderQuery;

    //Kafka 는 Default 세팅을 사용한다면 At Least Once 전략입니다.
    //TODO: 컨슈머의 멱등성이 아직 완전히 보장되지는 않는 것 같습니다.
    @KafkaListener(
            topics = "${spring.kafka.topic.point.cancel}",
            groupId = "${spring.kafka.group-id}",
            containerFactory = "pointListenerContainerFactory"
    )
    public void handle(OutBoxEventEntity event) {
        // Deserialize event payload if necessary
        String payload = event.getEventPayload();

        // TODO: 꺼내온 페이로드를 통해 알림 이벤트를 처리한다
//        List<Alert> alerts = alertSendSystem.getAlertListByRssSource(event.rssSource().getId());
//        if (!alerts.isEmpty()) {
//            alerts.forEach(alert ->
//                    alertSendSystem.sendAlertToPlatform(
//                            folderQuery.read(alert.getFolderId())
//                                    .orElseThrow(() -> new NoSuchDomainException(Folder.class))
//                                    .getName(),
//                            alert.getWebhookUrl(),
//                            event.posts()
//                    ));
//        }
    }
}
