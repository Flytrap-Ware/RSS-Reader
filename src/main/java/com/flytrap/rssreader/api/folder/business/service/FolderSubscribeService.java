package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.subscribe.infrastructure.repository.FolderSubscriptionEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderSubscribeService {

    private final FolderSubscriptionEntityJpaRepository folderSubscribeRepository;

    @Transactional
    public void folderUnsubscribe(Long subscribeId, Long folderId) {
        folderSubscribeRepository.deleteBySubscribeIdAndFolderId(subscribeId, folderId);
    }

}
