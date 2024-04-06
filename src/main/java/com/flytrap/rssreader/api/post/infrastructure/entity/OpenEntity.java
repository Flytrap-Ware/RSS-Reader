package com.flytrap.rssreader.api.post.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "open")
public class OpenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Builder
    protected OpenEntity(Long id, Long memberId, Long postId) {
        this.id = id;
        this.memberId = memberId;
        this.postId = postId;
    }

    public boolean isSameMember(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }
}
