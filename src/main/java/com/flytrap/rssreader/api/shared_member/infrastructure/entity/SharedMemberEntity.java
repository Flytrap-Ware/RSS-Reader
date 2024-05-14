package com.flytrap.rssreader.api.shared_member.infrastructure.entity;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberCreate;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberId;
import com.flytrap.rssreader.global.exception.domain.InconsistentDomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "shared_member")
public class SharedMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Builder
    protected SharedMemberEntity(Long id, Long folderId, Long accountId) {
        this.id = id;
        this.folderId = folderId;
        this.accountId = accountId;
    }

    public static SharedMemberEntity from(SharedMemberCreate sharedMemberCreate) {
        return SharedMemberEntity.builder()
            .folderId(sharedMemberCreate.folderId().value())
            .accountId(sharedMemberCreate.inviteeId().value())
            .build();
    }

    public SharedMember toReadOnly(Account account) {
        if (!Objects.equals(account.getId().value(), accountId)) {
            throw new InconsistentDomainException(SharedMember.class);
        }

        return SharedMember.builder()
            .id(new SharedMemberId(id))
            .accountId(account.getId())
            .name(account.getName().value())
            .profileUrl(account.getProfile())
            .build();
    }
}
