package com.flytrap.rssreader.api.folder.infrastructure.repository;

import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderEntityJpaRepository extends JpaRepository<FolderEntity, Long> {

    boolean existsByIdAndAccountIdAndIsDeletedFalse(Long id, Long accountId);
}
