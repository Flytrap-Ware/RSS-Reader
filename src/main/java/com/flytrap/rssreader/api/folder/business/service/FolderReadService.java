package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderReadService {

    private final FolderEntityJpaRepository repository;

    @Transactional(readOnly = true)
    public Folder findById(long id) {
        return repository.findByIdAndIsDeletedFalse(id).orElseThrow().toDomain();
    }

    @Transactional(readOnly = true)
    public List<Folder> findAllByMemberId(long memberId) {
        return repository.findAllByMemberIdAndIsDeletedFalse(memberId).stream()
                .map(FolderEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Folder> findAllByIds(Collection<Long> ids) {
        return repository.findAllById(ids).stream()
                .map(FolderEntity::toDomain)
                .toList();
    }
}
