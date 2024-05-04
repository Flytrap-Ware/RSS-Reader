package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.OpenEntity;
import com.flytrap.rssreader.api.post.infrastructure.output.OpenPostCountOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostOpenEntityRepository extends JpaRepository<OpenEntity, Long> {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    @Query("select p.subscriptionId as subscribeId, count(o.postId) as postCount "
            + "from OpenEntity o "
            + "inner join PostEntity p on o.postId = p.id "
            + "where o.memberId = :id and p.subscriptionId in :subscribes "
            + "group by p.subscriptionId")
    List<OpenPostCountOutput> countOpens(@Param("id") long id, @Param("subscribes") List<Long> subscribes);

    void deleteByMemberIdAndPostId(long memberId, Long postId);
}
