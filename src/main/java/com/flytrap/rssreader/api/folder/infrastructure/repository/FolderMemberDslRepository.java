package com.flytrap.rssreader.api.folder.infrastructure.repository;

import static com.flytrap.rssreader.api.folder.infrastructure.entity.QFolderMemberEntity.folderMemberEntity;
import static com.flytrap.rssreader.api.member.infrastructure.entity.QMemberEntity.memberEntity;

import com.flytrap.rssreader.api.folder.infrastructure.output.FolderMemberOutput;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Component;

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
                    memberEntity.name,
                    memberEntity.profile
                )
            ).from(folderMemberEntity)
            .join(memberEntity)
            .on(folderMemberEntity.memberId.eq(memberEntity.id))
            .where(folderMemberEntity.folderId.eq(folderId))
            .fetch();
    }

}
