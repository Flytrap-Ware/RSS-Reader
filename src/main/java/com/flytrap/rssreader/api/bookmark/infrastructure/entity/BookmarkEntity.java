package com.flytrap.rssreader.api.bookmark.infrastructure.entity;

import com.flytrap.rssreader.api.bookmark.domain.Bookmark;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.PostId;
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
@Table(name = "bookmark")
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Builder
    protected BookmarkEntity(Long id, Long memberId, Long postId) {
        this.id = id;
        this.memberId = memberId;
        this.postId = postId;
    }

    public static BookmarkEntity from(Bookmark bookmark) {
        return BookmarkEntity.builder()
            .id(bookmark.getId())
            .memberId(bookmark.getMemberId())
            .postId(bookmark.getPostId())
            .build();
    }

    public static BookmarkEntity create(AccountId accountId, PostId postId) {
        return BookmarkEntity.builder()
            .memberId(accountId.value())
            .postId(postId.value())
            .build();
    }

    public Bookmark toDomain() {
        return Bookmark.builder()
            .id(id)
            .memberId(memberId)
            .postId(postId)
            .build();
    }

    public boolean isSameMember(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }
}
