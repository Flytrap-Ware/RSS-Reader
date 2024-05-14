package com.flytrap.rssreader.api.alert.infrastructure.implement;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertJpaRepository;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AlertQuery {

    private final AlertJpaRepository alertJpaRepository;

    @Transactional(readOnly = true)
    public List<Alert> readAllByFolder(FolderId folderId) {
        return alertJpaRepository.findAllByFolderId(folderId.value())
            .stream()
            .map(AlertEntity::toReadOnly)
            .toList();
    }

}
