package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderValidation {

    private final FolderEntityJpaRepository folderJpaRepository;
    private final FolderMemberJpaRepository folderMemberJpaRepository;

    public boolean isAccessibleFolder(FolderId folderId, AccountId accountId) {
        return folderJpaRepository.existsByIdAndMemberIdAndIsDeletedFalse(folderId.value(),
            accountId.value()) || folderMemberJpaRepository.existsByFolderIdAndMemberId(
            folderId.value(), accountId.value());
    }

    public boolean isMyOwnFolder(FolderId folderId, AccountId accountId) {
        return folderJpaRepository.existsByIdAndMemberIdAndIsDeletedFalse(folderId.value(),
            accountId.value());
    }

}
