package com.flytrap.rssreader.api.shared_member.infrastructure.repository;

import static com.flytrap.rssreader.api.account.infrastructure.entity.QAccountEntity.accountEntity;
import static com.flytrap.rssreader.api.shared_member.infrastructure.entity.QSharedMemberEntity.sharedMemberEntity;

import com.flytrap.rssreader.api.shared_member.infrastructure.output.SharedMemberOutput;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Component;


@Component
public class SharedMemberDslRepository {

    private final JPAQueryFactory queryFactory;

    public SharedMemberDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<SharedMemberOutput> findAllByFolder(long folderId) {
        return queryFactory
            .selectDistinct(
                Projections.constructor(SharedMemberOutput.class,
                    sharedMemberEntity.id,
                    accountEntity.id,
                    accountEntity.name,
                    accountEntity.profile
                )
            ).from(sharedMemberEntity)
            .join(accountEntity)
            .on(sharedMemberEntity.accountId.eq(accountEntity.id))
            .where(sharedMemberEntity.folderId.eq(folderId))
            .fetch();
    }

}
