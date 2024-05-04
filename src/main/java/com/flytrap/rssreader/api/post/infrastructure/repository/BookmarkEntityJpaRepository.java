package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkEntityJpaRepository extends JpaRepository<BookmarkEntity, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void deleteByMemberIdAndPostId(Long memberId, Long postId);
}
