package com.flytrap.rssreader.api.alert.infrastructure.entity;

import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(name = "out_box")
public class OutBoxEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_iD", nullable = false)
    private UUID eventID;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "event_payload")
    private String eventPayload;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Builder
    public OutBoxEventEntity(Long id, UUID eventID, String eventType, String eventPayload, LocalDateTime creationDate, String status) {
        this.id = id;
        this.eventID = eventID;
        this.eventType = eventType;
        this.eventPayload = eventPayload;
        this.creationDate = creationDate;
        this.status = status;
    }

    public static OutBoxEventEntity from(NewPostAlertEvent event, String payload) {
        return OutBoxEventEntity.builder()
                .eventID(event.eventId())
                .eventType(event.getClass().getSimpleName())
                .eventPayload(payload)
                .creationDate(LocalDateTime.now())
                .status("알림")
                .build();
    }
}
