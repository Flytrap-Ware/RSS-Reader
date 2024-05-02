package com.flytrap.rssreader.api.folder.infrastructure.repository;

import static com.flytrap.rssreader.api.folder.infrastructure.entity.QFolderEntity.folderEntity;
import static com.flytrap.rssreader.api.folder.infrastructure.entity.QFolderMemberEntity.folderMemberEntity;

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
            .leftJoin(folderMemberEntity)
            .on(folderEntity.id.eq(folderMemberEntity.folderId))
            .where(folderEntity.memberId.eq(accountId)
                .or(folderMemberEntity.memberId.eq(accountId)))
            .fetch();
    }
}
