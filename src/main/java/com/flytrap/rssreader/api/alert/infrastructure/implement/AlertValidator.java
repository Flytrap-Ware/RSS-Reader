package com.flytrap.rssreader.api.alert.infrastructure.implement;

import com.flytrap.rssreader.api.alert.domain.AlertId;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AlertValidator {

    private final AlertJpaRepository alertJpaRepository;

    @Transactional(readOnly = true)
    public boolean exists(AlertId alertId) {
        return alertJpaRepository.existsById(alertId.value());
    }
}
