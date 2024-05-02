package com.flytrap.rssreader.api.subscribe.infrastructure.repository;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.FolderSubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderSubscriptionEntityJpaRepository extends
        JpaRepository<FolderSubscribeEntity, Long> {

    @Modifying
    @Query("DELETE FROM FolderSubscribeEntity e WHERE e.subscribeId = :subscribeId AND e.folderId = :folderId")
    void deleteBySubscribeIdAndFolderId(@Param("subscribeId") Long subscribeId,
            @Param("folderId") Long folderId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM FolderSubscribeEntity fs WHERE fs.folderId = :folderId")
    void deleteAllByFolderId(@Param("folderId") Long folderId);

    List<FolderSubscribeEntity> findAllByFolderId(Long folderId);

    List<FolderSubscribeEntity> findAllByFolderIdIn(List<Long> folderIds);
}
