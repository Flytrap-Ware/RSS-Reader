package com.flytrap.rssreader.api.alert.business.outbox.event;

import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent;
import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent1;
import com.flytrap.rssreader.api.alert.infrastructure.system.AlertSendSystem;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewPostAlertEventListener {

    private final KafkaProducer kafkaProducer;

//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @EventListener(NewPostAlertEvent.class)
    public void handle(NewPostAlertEvent event) {
        //TODO: 현재는 직접 쿼리를 DB 테이블에 접근해서 Alert하고있다 이것을 Kafka에 전송 하도록 바꾼다.
        log.debug("publishing task is New Post Alert Event");
        kafkaProducer.sendMessage(event);
    }
}
