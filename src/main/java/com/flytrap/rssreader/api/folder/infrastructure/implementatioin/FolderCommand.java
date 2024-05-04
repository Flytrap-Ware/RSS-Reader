package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FolderCommand {

    private final FolderEntityJpaRepository folderEntityJpaRepository;

    @Transactional
    public MyOwnFolder create(FolderCreate folderCreate) {
        return folderEntityJpaRepository.save(FolderEntity.from(folderCreate))
            .toMyOwnFolder();
    }

    @Transactional
    public MyOwnFolder update(MyOwnFolder myOwnFolder) {
        return folderEntityJpaRepository.save(FolderEntity.from(myOwnFolder))
            .toMyOwnFolder();
    }

    @Transactional
    public void delete(MyOwnFolder myOwnFolder) {
        folderEntityJpaRepository.save(FolderEntity.fromForDelete(myOwnFolder))
            .toMyOwnFolder();
    }

}
