package com.flytrap.rssreader.api.shared_member.infrastructure.repository;

import com.flytrap.rssreader.api.shared_member.infrastructure.entity.FolderMemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedMemberJpaRepository extends JpaRepository<FolderMemberEntity, Long> { // todo : rename to folderMemberJpaRepository

    long countAllByFolderId(long folderId);

    boolean existsByFolderIdAndMemberId(long folderId, long memberId);

    void deleteByFolderIdAndMemberId(long folderId, long memberId);

    Optional<FolderMemberEntity> findByFolderIdAndMemberId(long folderId, long memberId);

}
