package com.flytrap.rssreader.api.subscribe.infrastructure.repository;

import static com.flytrap.rssreader.api.subscribe.infrastructure.entity.QFolderSubscribeEntity.folderSubscribeEntity;
import static com.flytrap.rssreader.api.subscribe.infrastructure.entity.QSubscribeEntity.subscribeEntity;

import com.flytrap.rssreader.api.subscribe.infrastructure.output.FolderSubscriptionOutput;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FolderSubscriptionDslRepository {

    private final JPAQueryFactory queryFactory;

    public FolderSubscriptionDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public boolean existsByUrl(long folderId, String url) {
        Integer fetchOne = queryFactory
            .selectOne()
            .from(folderSubscribeEntity)
            .join(subscribeEntity)
            .on(folderSubscribeEntity.subscribeId.eq(subscribeEntity.id))
            .where(folderSubscribeEntity.folderId.eq(folderId).and(subscribeEntity.url.eq(url)))
            .fetchFirst();

        return fetchOne != null;
    }

    public List<FolderSubscriptionOutput> findAllByFolder(long folderId) {
        return queryFactory
            .selectDistinct(
                Projections.constructor(FolderSubscriptionOutput.class,
                    folderSubscribeEntity.id,
                    subscribeEntity.title,
                    subscribeEntity.url,
                    subscribeEntity.platform
                )
            ).from(folderSubscribeEntity)
            .join(subscribeEntity)
            .on(folderSubscribeEntity.subscribeId.eq(subscribeEntity.id))
            .where(folderSubscribeEntity.folderId.eq(folderId))
            .fetch();
    }
}
