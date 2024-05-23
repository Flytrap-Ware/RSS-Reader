package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderAggregate;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderCommandService {

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
            throw new ForbiddenAccessFolderException(Folder.class);

        FolderAggregate folderAggregate = folderCommand.readAggregate(folderId)
            .orElseThrow(() -> new NoSuchDomainException(FolderAggregate.class));
        folderAggregate.changeName(folderName);

        return folderCommand.update(folderAggregate);
    }

    public void deleteFolder(AccountId accountId, FolderId folderId) {
        if (!folderValidator.isMyOwnFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        FolderAggregate folderAggregate = folderCommand.readAggregate(folderId)
            .orElseThrow(() -> new NoSuchDomainException(FolderAggregate.class));

        folderCommand.delete(folderAggregate);
    }

}
