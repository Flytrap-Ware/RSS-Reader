package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderAggregate;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderUpdateService {

    private final FolderEntityJpaRepository repository;
    private final FolderValidator folderValidator;
    private final FolderCommand folderCommand;

    public FolderAggregate createNewFolder(AccountId accountId, String folderName) {
        FolderCreate folderCreate = FolderCreate.builder()
            .name(folderName)
            .ownerId(accountId)
            .build();

        return folderCommand.create(folderCreate);
    }

    public FolderAggregate updateFolder(AccountId accountId, FolderId folderId, String folderName) {
        if (!folderValidator.isMyOwnFolder(folderId, accountId))
            throw new IllegalArgumentException("폴더 수정 권한 없음"); // TODO: 예외 만들기

        FolderAggregate folderAggregate = folderCommand.readAggregate(folderId);
        folderAggregate.changeName(folderName);

        return folderCommand.update(folderAggregate);
    }

    public void deleteFolder(AccountId accountId, FolderId folderId) {
        if (!folderValidator.isMyOwnFolder(folderId, accountId))
            throw new IllegalArgumentException("폴더 수정 권한 없음"); // TODO: 예외 만들기

        FolderAggregate folderAggregate = folderCommand.readAggregate(folderId);

        folderCommand.delete(folderAggregate);
    }

    // TODO: 제거하기
    public void makePrivate(Folder folder) {
        if (folder.isShared()) {
            folder.toPrivate();
            repository.save(FolderEntity.from(folder));
        }
    }
}
