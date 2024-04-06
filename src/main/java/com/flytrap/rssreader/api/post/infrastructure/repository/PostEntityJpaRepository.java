package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostEntityJpaRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllBySubscribeOrderByPubDateDesc(SubscribeEntity subscribe);

    @Query("select p.subscribe.id as subscribeId, count(p.id) as postCount "
            + "from PostEntity p "
            + "where p.subscribe.id in :subscribes "
            + "group by p.subscribe.id")
    List<PostSubscribeCountOutput> findSubscribeCounts(@Param("subscribes") List<Long> subscribes);

}
