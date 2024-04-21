package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.folder.domain.AccessibleFolder;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.folder.infrastructure.repository.SharedFolderJpaRepository;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderReader {

    private final FolderEntityJpaRepository folderEntityJpaRepository;
    private final SharedFolderJpaRepository sharedFolderJpaRepository;

    public Folder read(FolderId folderId) {
        return folderEntityJpaRepository.findByIdAndIsDeletedFalse(folderId.value())
            .orElseThrow(() -> new NoSuchDomainException(Folder.class))
            .toDomain();
    }

    public AccessibleFolder readAccessible(FolderId folderId, AccountId accountId) {
        Folder folder = read(folderId);
        if (!folder.isOwner(accountId.value()) && !isSharedFolder(folderId, accountId)) {
            throw new ForbiddenAccessFolderException(folder);
        }

        return AccessibleFolder.from(folder);
    }

    private boolean isSharedFolder(FolderId folderId, AccountId accountId) {
        return sharedFolderJpaRepository.existsByFolderIdAndMemberId(folderId.value(), accountId.value());
    }

}
