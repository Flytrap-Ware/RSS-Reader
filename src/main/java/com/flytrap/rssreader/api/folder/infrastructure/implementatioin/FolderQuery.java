package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityDslRepository;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.shared_member.infrastructure.implementation.SharedMemberQuery;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionQuery;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderQuery {

    private final FolderEntityJpaRepository folderEntityJpaRepository;
    private final FolderEntityDslRepository folderEntityDslRepository;
    private final SubscriptionQuery subscriptionQuery;
    private final SharedMemberQuery sharedMemberQuery;

    public Folder read(FolderId folderId) {
        return folderEntityJpaRepository.findById(folderId.value())
            .orElseThrow(() -> new NoSuchDomainException(Folder.class))
            .toReadonly(
                subscriptionQuery.readAllByFolder(folderId),
                sharedMemberQuery.readAllByFolder(folderId)
            );
    }

    public List<Folder> readAllByAccount(AccountId accountId) {
        return folderEntityDslRepository
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
