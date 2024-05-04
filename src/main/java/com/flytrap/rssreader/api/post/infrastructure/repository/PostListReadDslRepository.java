package com.flytrap.rssreader.api.post.infrastructure.repository;

import static com.flytrap.rssreader.api.folder.infrastructure.entity.QFolderEntity.folderEntity;
import static com.flytrap.rssreader.api.folder.infrastructure.entity.QFolderMemberEntity.folderMemberEntity;
import static com.flytrap.rssreader.api.post.infrastructure.entity.QBookmarkEntity.bookmarkEntity;
import static com.flytrap.rssreader.api.post.infrastructure.entity.QOpenEntity.openEntity;
import static com.flytrap.rssreader.api.post.infrastructure.entity.QPostEntity.postEntity;
import static com.flytrap.rssreader.api.subscribe.infrastructure.entity.QFolderSubscribeEntity.folderSubscribeEntity;
import static com.flytrap.rssreader.api.subscribe.infrastructure.entity.QSubscribeEntity.subscribeEntity;

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
            .and(folderEntity.memberId.eq(accountId))
            .or(folderMemberEntity.memberId.eq(accountId));

        addFilterCondition(builder, postFilter, accountId);

        return initFindAllQuery()
            .join(folderSubscribeEntity)
            .on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
            .join(folderEntity).on(folderSubscribeEntity.folderId.eq(folderEntity.id))
            .leftJoin(folderMemberEntity).on(folderEntity.id.eq(folderMemberEntity.folderId))
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
            .and(folderSubscribeEntity.folderId.eq(folderId));

        addFilterCondition(builder, postFilter, accountId);

        return initFindAllQuery()
            .join(folderSubscribeEntity)
            .on(subscribeEntity.id.eq(folderSubscribeEntity.subscribeId))
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
            .and(postEntity.subscriptionId.eq(subscribeId));

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
            .and(bookmarkEntity.memberId.eq(accountId));

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
                    postEntity.subscriptionId,
                    postEntity.guid,
                    postEntity.title,
                    postEntity.thumbnailUrl,
                    postEntity.description,
                    postEntity.pubDate,
                    subscribeEntity.title,
                    Expressions.booleanTemplate("{0} is not null", openEntity.id),
                    Expressions.booleanTemplate("{0} is not null", bookmarkEntity.id)
                )
            )
            .from(postEntity)
            .join(subscribeEntity).on(postEntity.subscriptionId.eq(subscribeEntity.id))
            .leftJoin(openEntity).on(postEntity.id.eq(openEntity.postId))
            .leftJoin(bookmarkEntity).on(postEntity.id.eq(bookmarkEntity.postId));
    }

    private void addFilterCondition(BooleanBuilder builder, PostFilter postFilter, long accountId) {

        builder
            .and(openEntity.memberId.eq(accountId).or(openEntity.memberId.isNull()))
            .and(bookmarkEntity.memberId.eq(accountId).or(bookmarkEntity.memberId.isNull()));

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
