package com.flytrap.rssreader.api.folder.infrastructure.repository;

import com.flytrap.rssreader.api.folder.infrastructure.entity.SharedFolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedFolderJpaRepository extends JpaRepository<SharedFolderEntity, Long> { // todo : rename to folderMemberJpaRepository

    long countAllByFolderId(long folderId);

    List<SharedFolderEntity> findAllByFolderId(long folderId);

    boolean existsByFolderIdAndMemberId(long folderId, long memberId);

    Optional<SharedFolderEntity> findByFolderIdAndMemberId(long folderId, long memberId);

    List<SharedFolderEntity> findAllByMemberId(long memberId);
    List<SharedFolderEntity> findAllByFolderIdIn(List<Long> folderIds);

}
