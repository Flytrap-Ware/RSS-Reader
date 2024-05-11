package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.OpenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostOpenEntityRepository extends JpaRepository<OpenEntity, Long> {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void deleteByMemberIdAndPostId(long memberId, Long postId);
}
