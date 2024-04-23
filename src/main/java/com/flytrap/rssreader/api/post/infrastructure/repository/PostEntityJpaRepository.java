package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostEntityJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllBySubscriptionId(long subscriptionId);

    @Query("select p.subscriptionId as subscriptionId, count(p.id) as postCount "
            + "from PostEntity p "
            + "where p.subscriptionId in :subscribeIds "
            + "group by p.subscriptionId")
    List<PostSubscribeCountOutput> countBySubscriptions(@Param("subscribeIds") List<Long> subscribeIds);

}
