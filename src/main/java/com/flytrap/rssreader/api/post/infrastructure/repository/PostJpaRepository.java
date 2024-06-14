package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, String> {
    Optional<PostEntity> findFirstByRssSourceIdOrderByPubDateDesc(long rssSourceId);
}
