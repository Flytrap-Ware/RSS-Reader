package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderRequest;
import com.flytrap.rssreader.api.member.domain.AccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderUpdateService {

    private final FolderEntityJpaRepository repository;
    private final FolderCommand folderCommand;

    public MyOwnFolder createNewFolder(AccountId accountId, String folderName) {
        FolderCreate folderCreate = FolderCreate.builder()
            .name(folderName)
            .ownerId(accountId)
            .build();

        return folderCommand.save(folderCreate);
    }

    @Transactional
    public Folder updateFolder(FolderRequest.CreateRequest request, Folder folder, long memberId) {
        folder.updateName(request.name());

        return repository.save(FolderEntity.from(folder)).toDomain();
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
