package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.FolderSubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.FolderSubscriptionEntityJpaRepository;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderSubscribeService {

    private final FolderSubscriptionEntityJpaRepository folderSubscribeRepository;

    @Transactional
    public void folderSubscribe(Subscribe subscribe, Long folderId) {
        folderSubscribeRepository.save(FolderSubscribeEntity.from(subscribe, folderId));
    }

    @Transactional
    public void folderUnsubscribe(Long subscribeId, Long folderId) {
        folderSubscribeRepository.deleteBySubscribeIdAndFolderId(subscribeId, folderId);
    }

    @Transactional
    public void unsubscribeAllByFolder(Folder folder) {
        folderSubscribeRepository.deleteAllByFolderId(folder.getId());
    }

    @Transactional(readOnly = true)
    public List<Long> getFolderSubscribeId(Long folderId) {
        return folderSubscribeRepository.findAllByFolderId(folderId)
                .stream()
                .map(FolderSubscribeEntity::getId)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<Folder, List<Long>> getFolderSubscribeIds(List<? extends Folder> folders) {
        List<Long> folderIds = folders.stream()
                .map(Folder::getId)
                .toList();

        List<FolderSubscribeEntity> folderSubscribes =
                folderSubscribeRepository.findAllByFolderIdIn(folderIds);

        Map<Long, List<Long>> folderSubscribeIds = folderSubscribes.stream()
                .collect(Collectors.groupingBy(FolderSubscribeEntity::getFolderId,
                        Collectors.mapping(FolderSubscribeEntity::getSubscribeId,
                                Collectors.toList())));

        return folders.stream()
                .collect(Collectors.toMap(folder -> folder,
                        folder -> folderSubscribeIds.getOrDefault(folder.getId(), List.of())));
    }
}
