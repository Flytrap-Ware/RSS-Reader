package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.OpenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostOpenJpaRepository extends JpaRepository<OpenEntity, Long> {

    boolean existsByAccountIdAndPostId(Long accountId, Long postId);

    void deleteByAccountIdAndPostId(long memberId, Long postId);
}
