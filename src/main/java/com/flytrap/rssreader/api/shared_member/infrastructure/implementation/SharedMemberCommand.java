package com.flytrap.rssreader.api.shared_member.infrastructure.implementation;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.infrastructure.repository.AccountQuery;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberCreate;
import com.flytrap.rssreader.api.shared_member.infrastructure.entity.FolderMemberEntity;
import com.flytrap.rssreader.api.shared_member.infrastructure.repository.SharedMemberJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SharedMemberCommand {

    private final SharedMemberJpaRepository sharedMemberJpaRepository;
    private final AccountQuery accountQuery;

    @Transactional
    public SharedMember create(SharedMemberCreate sharedMemberCreate) {
        Account account = accountQuery.readById(sharedMemberCreate.inviteeId())
            .orElseThrow(() -> new NoSuchDomainException(Account.class));

        return sharedMemberJpaRepository
            .save(FolderMemberEntity.from(sharedMemberCreate))
            .toReadOnly(account);
    }

    @Transactional
    public void deleteBy(FolderId folderId, AccountId accountId) {
        sharedMemberJpaRepository
            .deleteByFolderIdAndMemberId(folderId.value(), accountId.value());
    }

}
