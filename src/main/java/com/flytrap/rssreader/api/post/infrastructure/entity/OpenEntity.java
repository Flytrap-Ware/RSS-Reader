package com.flytrap.rssreader.api.post.infrastructure.entity;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.PostId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "open")
public class OpenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Builder
    protected OpenEntity(Long id, Long accountId, Long postId) {
        this.id = id;
        this.accountId = accountId;
        this.postId = postId;
    }

    public static OpenEntity from(AccountId accountId, PostId postId) {
        return OpenEntity.builder()
            .accountId(accountId.value())
            .postId(postId.value())
            .build();
    }
}
