package com.flytrap.rssreader.api.folder.infrastructure.repository;

import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderEntityJpaRepository extends JpaRepository<FolderEntity, Long> {

    Optional<FolderEntity> findByIdAndIsDeletedFalse(Long id);
    List<FolderEntity> findAllByMemberIdAndIsDeletedFalse(Long memberId);
}
