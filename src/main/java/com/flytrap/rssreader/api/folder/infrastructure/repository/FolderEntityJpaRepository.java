package com.flytrap.rssreader.api.folder.infrastructure.repository;

import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderEntityJpaRepository extends JpaRepository<FolderEntity, Long> {

    Optional<FolderEntity> findByIdAndIsDeletedFalse(Long id);
    boolean existsByIdAndMemberIdAndIsDeletedFalse(Long id, Long memberId);
}
