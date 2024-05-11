package com.flytrap.rssreader.api.shared_member.infrastructure.entity;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberCreate;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberId;
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
@Table(name = "folder_member")
public class FolderMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long folderId;
    private Long memberId;

    @Builder
    protected FolderMemberEntity(Long id, Long folderId, Long memberId) {
        this.id = id;
        this.folderId = folderId;
        this.memberId = memberId;
    }

    public static FolderMemberEntity from(SharedMemberCreate sharedMemberCreate) {
        return FolderMemberEntity.builder()
            .folderId(sharedMemberCreate.folderId().value())
            .memberId(sharedMemberCreate.inviteeId().value())
            .build();
    }

    public SharedMember toReadOnly(Account account) {
        if (!Objects.equals(account.getId().value(), memberId)) {
            throw new RuntimeException("정합성 일치하지 않음.");
        }

        return SharedMember.builder()
            .id(new SharedMemberId(id))
            .accountId(account.getId())
            .name(account.getName().value())
            .profileUrl(account.getProfile())
            .build();
    }
}
