package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderDslRepository;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderJpaRepository;
import com.flytrap.rssreader.api.shared_member.infrastructure.implementation.SharedMemberQuery;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderQuery {

    private final FolderJpaRepository folderJpaRepository;
    private final FolderDslRepository folderDslRepository;
    private final SubscriptionQuery subscriptionQuery;
    private final SharedMemberQuery sharedMemberQuery;

    public Optional<Folder> read(FolderId folderId) {
        return folderJpaRepository.findById(folderId.value())
            .map(entity ->
                entity.toReadonly(
                    subscriptionQuery.readAllByFolder(folderId),
                    sharedMemberQuery.readAllByFolder(folderId))
            );
    }

    public List<Folder> readAllByAccount(AccountId accountId) {
        return folderDslRepository
            .findAllAccessibleFolder(accountId.value())
            .stream()
            .map(folderEntity -> {
                FolderId folderId = new FolderId(folderEntity.getId());

                if (folderEntity.getIsShared()) {
                    return folderEntity.toReadonly(
                        subscriptionQuery.readAllByFolder(folderId),
                        sharedMemberQuery.readAllByFolder(folderId));
                } else {
                    return folderEntity.toReadonly(
                        subscriptionQuery.readAllByFolder(folderId),
                        List.of());
                }
            })
            .toList();
    }

}
