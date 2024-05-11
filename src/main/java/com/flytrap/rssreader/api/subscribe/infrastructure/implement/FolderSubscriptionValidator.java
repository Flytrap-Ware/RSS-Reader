package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.FolderSubscriptionDslRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FolderSubscriptionValidator {

    private final SubscribeEntityJpaRepository subscribeEntityJpaRepository;
    private final FolderSubscriptionDslRepository folderSubscriptionDslRepository;

    @Transactional(readOnly = true)
    public boolean existsBy(FolderId folderId, String rssUrl) {
        return folderSubscriptionDslRepository.existsByUrl(folderId.value(), rssUrl);
    }
}
