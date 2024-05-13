package com.flytrap.rssreader.api.alert.infrastructure.implement;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertCreate;
import com.flytrap.rssreader.api.alert.domain.AlertId;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AlertCommand {

    private final AlertEntityJpaRepository alertJpaRepository;

    @Transactional
    public Alert create(AlertCreate alertCreate) {
        return alertJpaRepository.save(AlertEntity.from(alertCreate))
            .toReadOnly();
    }

    @Transactional
    public void delete(AlertId alertId) {
        alertJpaRepository.deleteById(alertId.value());
    }
}
