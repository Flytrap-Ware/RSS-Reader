package com.flytrap.rssreader.global.kafka;

import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent;
import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent1;
import com.flytrap.rssreader.api.alert.infrastructure.entity.OutBoxEventEntity;
import com.flytrap.rssreader.api.alert.infrastructure.repository.OutBoxEventJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class KafkaProducer {
    private final OutBoxEventJpaRepository outBoxEventJpaRepository;
    private final KafkaTemplate<String, OutBoxEventEntity> kafkaTemplate;
    private final String pointCancelTopic;

    public KafkaProducer(OutBoxEventJpaRepository outBoxEventJpaRepository,
                                    KafkaTemplate<String, OutBoxEventEntity> kafkaTemplate,
                                    @Value("${spring.kafka.topic.point.cancel}") String pointCancelTopic) {
        this.outBoxEventJpaRepository = outBoxEventJpaRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.pointCancelTopic = pointCancelTopic;
    }

    //RDB에서 Out Box용 메시지는 제거한다 중복된 내용이 들어가면 안되기 때문에
    @Transactional
    public void sendMessage(NewPostAlertEvent event) {
        List<OutBoxEventEntity> messages = outBoxEventJpaRepository.findAll();
        messages.forEach(message -> kafkaTemplate.send(pointCancelTopic, message));
        outBoxEventJpaRepository.deleteByIdIn(messages.stream().map(OutBoxEventEntity::getId).toList());
    }
}


