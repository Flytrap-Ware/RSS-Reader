package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.member.domain.AccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderUpdateService {

    private final FolderEntityJpaRepository repository;
    private final FolderQuery folderQuery;
    private final FolderCommand folderCommand;

    public MyOwnFolder createNewFolder(AccountId accountId, String folderName) {
        FolderCreate folderCreate = FolderCreate.builder()
            .name(folderName)
            .ownerId(accountId)
            .build();

        return folderCommand.create(folderCreate);
    }

    @Transactional
    public MyOwnFolder updateFolder(AccountId accountId, FolderId folderId, String folderName) {
        MyOwnFolder myOwnFolder = folderQuery.readMyOwn(folderId, accountId);
        myOwnFolder.changeName(folderName);

        return folderCommand.update(myOwnFolder);
    }

    @Transactional
    public Folder deleteFolder(Folder folder, long id) {
        folder.delete();

        return repository.save(FolderEntity.from(folder)).toDomain();
    }

    public void shareFolder(Folder folder) {
        if (!folder.isShared()) {
            folder.toShare();
            repository.save(FolderEntity.from(folder));
        }
    }

    public void makePrivate(Folder folder) {
        if (folder.isShared()) {
            folder.toPrivate();
            repository.save(FolderEntity.from(folder));
        }
    }
}
