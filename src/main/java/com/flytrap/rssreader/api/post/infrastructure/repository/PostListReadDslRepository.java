package com.flytrap.rssreader.api.post.infrastructure.repository;

import static com.flytrap.rssreader.api.folder.infrastructure.entity.QFolderEntity.folderEntity;
import static com.flytrap.rssreader.api.post.infrastructure.entity.QBookmarkEntity.bookmarkEntity;
import static com.flytrap.rssreader.api.post.infrastructure.entity.QOpenEntity.openEntity;
import static com.flytrap.rssreader.api.post.infrastructure.entity.QPostEntity.postEntity;
import static com.flytrap.rssreader.api.shared_member.infrastructure.entity.QSharedMemberEntity.sharedMemberEntity;
import static com.flytrap.rssreader.api.subscribe.infrastructure.entity.QRssSourceEntity.rssSourceEntity;
import static com.flytrap.rssreader.api.subscribe.infrastructure.entity.QSubscriptionEntity.subscriptionEntity;

import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSummaryOutput;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class PostListReadDslRepository implements PostListReadRepository {

    private final JPAQueryFactory queryFactory;

    public PostListReadDslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<PostSummaryOutput> findById(Long postId) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(postEntity.id.eq(postId));

        return Optional.ofNullable(initFindAllQuery()
            .where(builder)
            .fetchOne());
    }

    public List<PostSummaryOutput> findAllByAccount(long accountId, PostFilter postFilter,
        Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(folderEntity.isDeleted.eq(false))
            .and(folderEntity.accountId.eq(accountId))
            .or(sharedMemberEntity.accountId.eq(accountId));

        addFilterCondition(builder, postFilter, accountId);

        return initFindAllQuery()
            .join(subscriptionEntity)
            .on(rssSourceEntity.id.eq(subscriptionEntity.rssSourceId))
            .join(folderEntity).on(subscriptionEntity.folderId.eq(folderEntity.id))
            .leftJoin(sharedMemberEntity).on(folderEntity.id.eq(sharedMemberEntity.folderId))
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public List<PostSummaryOutput> findAllByFolder(long accountId, long folderId,
        PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(subscriptionEntity.folderId.eq(folderId));

        addFilterCondition(builder, postFilter, accountId);

        return initFindAllQuery()
            .join(subscriptionEntity)
            .on(rssSourceEntity.id.eq(subscriptionEntity.rssSourceId))
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public List<PostSummaryOutput> findAllBySubscription(long accountId, long subscribeId,
        PostFilter postFilter, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(postEntity.rssSourceId.eq(subscribeId));

        addFilterCondition(builder, postFilter, accountId);

        return initFindAllQuery()
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public List<PostSummaryOutput> findAllBookmarked(long accountId, PostFilter postFilter,
        Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(bookmarkEntity.accountId.eq(accountId));

        addFilterCondition(builder, postFilter, accountId);

        return initFindAllQuery()
            .where(builder)
            .orderBy(postEntity.pubDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private JPAQuery<PostSummaryOutput> initFindAllQuery() {
        return queryFactory
            .selectDistinct(
                Projections.constructor(PostSummaryOutput.class,
                    postEntity.id,
                    postEntity.rssSourceId,
                    postEntity.guid,
                    postEntity.title,
                    postEntity.thumbnailUrl,
                    postEntity.description,
                    postEntity.pubDate,
                    rssSourceEntity.title,
                    Expressions.booleanTemplate("{0} is not null", openEntity.id),
                    Expressions.booleanTemplate("{0} is not null", bookmarkEntity.id)
                )
            )
            .from(postEntity)
            .join(rssSourceEntity).on(postEntity.rssSourceId.eq(rssSourceEntity.id))
            .leftJoin(openEntity).on(postEntity.id.eq(openEntity.postId))
            .leftJoin(bookmarkEntity).on(postEntity.id.eq(bookmarkEntity.postId));
    }

    private void addFilterCondition(BooleanBuilder builder, PostFilter postFilter, long accountId) {

        builder
            .and(openEntity.accountId.eq(accountId).or(openEntity.accountId.isNull()))
            .and(bookmarkEntity.accountId.eq(accountId).or(bookmarkEntity.accountId.isNull()));

        if (postFilter.hasKeyword()) {
            builder
                .and(postEntity.title.contains(postFilter.keyword()))
                .and(postEntity.description.contains(postFilter.keyword()));
        }

        if (postFilter.hasDataRange()) {
            builder
                .and(postEntity.pubDate.between(
                    Instant.ofEpochMilli(postFilter.start()),
                    Instant.ofEpochMilli(postFilter.end()))
                );
        }

        if (postFilter.hasReadCondition()) {
            builder.and(openEntity.isNotNull());
        } else if (postFilter.hasUnReadCondition()) {
            builder.and(openEntity.isNull());
        }
    }
}
