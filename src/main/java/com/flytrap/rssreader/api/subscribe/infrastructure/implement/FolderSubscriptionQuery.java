package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.api.subscribe.infrastructure.output.FolderSubscriptionOutput;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.FolderSubscriptionDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderSubscriptionQuery {

    private final FolderSubscriptionDslRepository folderSubscriptionDslRepository;

    public List<FolderSubscription> readAllByFolder(FolderId folderId) {
        return folderSubscriptionDslRepository
            .findAllByFolder(folderId.value())
            .stream()
            .map(FolderSubscriptionOutput::toDomain)
            .toList();
    }
}
