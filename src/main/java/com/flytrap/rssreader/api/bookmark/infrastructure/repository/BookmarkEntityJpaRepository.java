package com.flytrap.rssreader.api.bookmark.infrastructure.repository;

import com.flytrap.rssreader.api.bookmark.infrastructure.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkEntityJpaRepository extends JpaRepository<BookmarkEntity, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void deleteAllByMemberIdAndPostId(Long memberId, Long postId);
}
