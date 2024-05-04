package com.flytrap.rssreader.api.folder.infrastructure.repository;

import com.flytrap.rssreader.api.folder.infrastructure.output.FolderMemberOutput;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Component;

import static com.flytrap.rssreader.api.account.infrastructure.entity.QAccountEntity.accountEntity;
import static com.flytrap.rssreader.api.folder.infrastructure.entity.QFolderMemberEntity.folderMemberEntity;


@Component
public class FolderMemberDslRepository {

    private final JPAQueryFactory queryFactory;

    public FolderMemberDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<FolderMemberOutput> findAllByFolder(long folderId) {
        return queryFactory
            .selectDistinct(
                Projections.constructor(FolderMemberOutput.class,
                    folderMemberEntity.id,
                        accountEntity.name,
                        accountEntity.profile
                )
            ).from(folderMemberEntity)
            .join(accountEntity)
            .on(folderMemberEntity.memberId.eq(accountEntity.id))
            .where(folderMemberEntity.folderId.eq(folderId))
            .fetch();
    }

}
