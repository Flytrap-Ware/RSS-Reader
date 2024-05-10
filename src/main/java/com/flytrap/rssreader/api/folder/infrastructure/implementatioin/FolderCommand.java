package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderAggregate;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FolderCommand {

    private final FolderEntityJpaRepository folderEntityJpaRepository;

    @Transactional
    public FolderAggregate readAggregate(FolderId folderId) {
        return folderEntityJpaRepository.findById(folderId.value())
            .orElseThrow(() -> new NoSuchDomainException(Folder.class))
            .toAggregate();
    }

    @Transactional
    public FolderAggregate create(FolderCreate folderCreate) {
        return folderEntityJpaRepository.save(FolderEntity.from(folderCreate))
            .toAggregate();
    }

    @Transactional
    public FolderAggregate update(FolderAggregate folderAggregate) {
        return folderEntityJpaRepository.save(FolderEntity.from(folderAggregate))
            .toAggregate();
    }

    @Transactional
    public void delete(FolderAggregate folderAggregate) {
        folderEntityJpaRepository.save(FolderEntity.fromForDelete(folderAggregate));
    }

}
