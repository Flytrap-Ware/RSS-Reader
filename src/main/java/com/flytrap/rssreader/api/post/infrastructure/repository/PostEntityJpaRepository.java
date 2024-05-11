package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllBySubscriptionId(long subscriptionId);

}
