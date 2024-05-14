package com.flytrap.rssreader.api.shared_member.infrastructure.repository;

import com.flytrap.rssreader.api.shared_member.infrastructure.entity.SharedMemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedMemberJpaRepository extends JpaRepository<SharedMemberEntity, Long> {

    long countAllByFolderId(long folderId);

    boolean existsByFolderIdAndAccountId(long folderId, long accountId);

    void deleteByFolderIdAndAccountId(long folderId, long accountId);

    Optional<SharedMemberEntity> findByFolderIdAndAccountId(long folderId, long accountId);

}
