package com.flytrap.rssreader.api.alert.infrastructure.implement;

import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertQuery {

    private final AlertEntityJpaRepository alertRepository;

    public List<Alert> readAllByFolder(FolderId folderId) {
        return alertRepository.findAllByFolderId(folderId.value())
            .stream()
            .map(AlertEntity::toReadOnly)
            .toList();
    }

}
