package com.flytrap.rssreader.api.subscribe.infrastructure.repository;


import static com.flytrap.rssreader.api.subscribe.infrastructure.entity.QRssSourceEntity.rssSourceEntity;
import static com.flytrap.rssreader.api.subscribe.infrastructure.entity.QSubscriptionEntity.*;

import com.flytrap.rssreader.api.subscribe.infrastructure.output.SubscriptionOutput;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDslRepository {

    private final JPAQueryFactory queryFactory;

    public SubscriptionDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public boolean existsByUrl(long folderId, String url) {
        Integer fetchOne = queryFactory
            .selectOne()
            .from(subscriptionEntity)
            .join(rssSourceEntity)
            .on(subscriptionEntity.rssSourceId.eq(rssSourceEntity.id))
            .where(subscriptionEntity.folderId.eq(folderId).and(rssSourceEntity.url.eq(url)))
            .fetchFirst();

        return fetchOne != null;
    }

    public List<SubscriptionOutput> findAllByFolder(long folderId) {
        return queryFactory
            .selectDistinct(
                Projections.constructor(SubscriptionOutput.class,
                    subscriptionEntity.id,
                    rssSourceEntity.title,
                    rssSourceEntity.url,
                    rssSourceEntity.platform
                )
            ).from(subscriptionEntity)
            .join(rssSourceEntity)
            .on(subscriptionEntity.rssSourceId.eq(rssSourceEntity.id))
            .where(subscriptionEntity.folderId.eq(folderId))
            .fetch();
    }
}
