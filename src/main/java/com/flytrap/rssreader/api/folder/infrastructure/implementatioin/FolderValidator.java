package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.shared_member.infrastructure.repository.SharedMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FolderValidator {

    private final FolderEntityJpaRepository folderJpaRepository;
    private final SharedMemberJpaRepository sharedMemberJpaRepository;

    @Transactional(readOnly = true)
    public boolean isAccessibleFolder(FolderId folderId, AccountId accountId) {
        return folderJpaRepository
            .existsByIdAndMemberIdAndIsDeletedFalse(folderId.value(), accountId.value())
            || sharedMemberJpaRepository
            .existsByFolderIdAndMemberId(folderId.value(), accountId.value());
    }

    @Transactional(readOnly = true)
    public boolean isMyOwnFolder(FolderId folderId, AccountId accountId) {
        return folderJpaRepository.existsByIdAndMemberIdAndIsDeletedFalse(folderId.value(),
            accountId.value());
    }

}
