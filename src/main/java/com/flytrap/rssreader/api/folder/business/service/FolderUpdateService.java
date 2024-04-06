package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderUpdateService {

    private final FolderEntityJpaRepository repository;

    public Folder createNewFolder(@Valid FolderRequest.CreateRequest request, long member) {
        Folder folder = Folder.create(request.name(), member);
        return repository.save(FolderEntity.from(folder)).toDomain();
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
