package com.flytrap.rssreader.api.shared_member.infrastructure.implementation;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.shared_member.infrastructure.repository.SharedMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SharedMemberValidator {

    private final SharedMemberJpaRepository sharedMemberJpaRepository;

    @Transactional(readOnly = true)
    public boolean existsBy(FolderId folderId, AccountId inviteeId) {
        return sharedMemberJpaRepository
            .existsByFolderIdAndAccountId(folderId.value(), inviteeId.value());
    }

    @Transactional(readOnly = true)
    public boolean hasNoSharedMembersByFolder(FolderId folderId) {
        return sharedMemberJpaRepository
            .countAllByFolderId(folderId.value()) <= 0;
    }

}
