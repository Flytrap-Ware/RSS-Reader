package com.flytrap.rssreader.api.reaction.infrastructure.entity;

import com.flytrap.rssreader.api.account.infrastructure.entity.AccountEntity;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "reaction")
public class ReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private AccountEntity member;

    @Column(length = 25)
    private String emoji;

    @Builder
    public ReactionEntity(Long id, PostEntity post, AccountEntity member, String emoji) {
        this.id = id;
        this.post = post;
        this.member = member;
        this.emoji = emoji;
    }

    public static ReactionEntity create(PostEntity post, AccountEntity member, String emoji) {
        return ReactionEntity.builder()
                .post(post)
                .member(member)
                .emoji(emoji)
                .build();
    }
}
