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
@Table(name = "bookmark")
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Builder
    protected BookmarkEntity(Long id, Long accountId, Long postId) {
        this.id = id;
        this.accountId = accountId;
        this.postId = postId;
    }

    public static BookmarkEntity create(AccountId accountId, PostId postId) {
        return BookmarkEntity.builder()
            .accountId(accountId.value())
            .postId(postId.value())
            .build();
    }

}
