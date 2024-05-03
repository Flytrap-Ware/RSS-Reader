package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderCommand {

    private final FolderEntityJpaRepository folderEntityJpaRepository;

    public MyOwnFolder save(FolderCreate folderCreate) {
        return folderEntityJpaRepository.save(FolderEntity.from(folderCreate))
            .toMyOwnFolder();
    }

}
