package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderDomain;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityDslRepository;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.FolderSubscriptionQuery;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderQuery {

    private final FolderEntityJpaRepository folderEntityJpaRepository;
    private final FolderEntityDslRepository folderEntityDslRepository;
    private final FolderSubscriptionQuery folderSubscriptionQuery;
    private final FolderMemberQuery folderMemberQuery;

    public FolderDomain read(FolderId folderId) {
        return folderEntityJpaRepository.findById(folderId.value())
            .orElseThrow(() -> new NoSuchDomainException(Folder.class))
            .toReadonly(
                folderSubscriptionQuery.readAllByFolder(folderId),
                folderMemberQuery.readAllByFolder(folderId)
            );
    }

    public List<FolderDomain> readAllByAccount(AccountId accountId) {
        return folderEntityDslRepository
            .findAllAccessibleFolder(accountId.value())
            .stream()
            .map(folderEntity -> {
                FolderId folderId = new FolderId(folderEntity.getId());

                if (folderEntity.getIsShared()) {
                    return folderEntity.toReadonly(
                        folderSubscriptionQuery.readAllByFolder(folderId),
                        folderMemberQuery.readAllByFolder(folderId));
                } else {
                    return folderEntity.toReadonly(
                        folderSubscriptionQuery.readAllByFolder(folderId),
                        List.of());
                }
            })
            .toList();
    }

}
