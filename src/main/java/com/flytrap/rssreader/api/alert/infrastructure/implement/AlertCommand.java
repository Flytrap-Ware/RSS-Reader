package com.flytrap.rssreader.api.alert.infrastructure.implement;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertCreate;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertCommand {

    private final AlertEntityJpaRepository alertRepository;

    public Alert create(AlertCreate alertCreate) {
        return alertRepository.save(AlertEntity.from(alertCreate))
            .toReadOnly();
    }
}
