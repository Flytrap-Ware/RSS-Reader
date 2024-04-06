package com.flytrap.rssreader.api.alert.infrastructure.repository;

import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "alert_service")
public class AlertServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private AlertPlatform alertPlatform;
}
