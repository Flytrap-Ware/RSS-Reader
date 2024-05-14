package com.flytrap.rssreader.api.folder.infrastructure.repository;

import static com.flytrap.rssreader.api.folder.infrastructure.entity.QFolderEntity.folderEntity;
import static com.flytrap.rssreader.api.shared_member.infrastructure.entity.QSharedMemberEntity.sharedMemberEntity;

import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FolderEntityDslRepository {

    private final JPAQueryFactory queryFactory;

    public FolderEntityDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<FolderEntity> findAllAccessibleFolder(long accountId) {
        return queryFactory
            .select(folderEntity)
            .from(folderEntity)
            .leftJoin(sharedMemberEntity)
            .on(folderEntity.id.eq(sharedMemberEntity.folderId))
            .where(folderEntity.accountId.eq(accountId)
                .or(sharedMemberEntity.accountId.eq(accountId))
                .and(folderEntity.isDeleted.eq(false)))
            .fetch();
    }
}
