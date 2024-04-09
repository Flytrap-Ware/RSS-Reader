package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostEntityJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllBySubscribe(SubscribeEntity subscribe);

    @Query("select p.subscribe.id as subscribeId, count(p.id) as postCount "
            + "from PostEntity p "
            + "where p.subscribe.id in :subscribes "
            + "group by p.subscribe.id")
    List<PostSubscribeCountOutput> findSubscribeCounts(@Param("subscribes") List<Long> subscribes);

}
