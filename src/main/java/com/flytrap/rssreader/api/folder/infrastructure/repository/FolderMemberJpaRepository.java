package com.flytrap.rssreader.api.folder.infrastructure.repository;

import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderMemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderMemberJpaRepository extends JpaRepository<FolderMemberEntity, Long> { // todo : rename to folderMemberJpaRepository

    long countAllByFolderId(long folderId);

    boolean existsByFolderIdAndMemberId(long folderId, long memberId);

    Optional<FolderMemberEntity> findByFolderIdAndMemberId(long folderId, long memberId);

}
