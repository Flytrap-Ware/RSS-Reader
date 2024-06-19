package com.flytrap.rssreader.api.post.business.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.flytrap.rssreader.CustomServiceTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.post.PostScriptSql;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostCommand;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;


@CustomServiceTest
class PostListQueryServiceTest {

    @Autowired
    PostListQueryService postListQueryService;

    @Autowired
    PostCommand postCommand;

    @Nested
    @PostScriptSql
    class getPostsByAccount {

        @Test
        void 회원이_볼수있는_모든_게시글을_조회할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var postFilter = new PostFilter(null, null, null, null);
            var pageable = Pageable.ofSize(15);

            // when
            var posts = postListQueryService
                .getPostsByAccount(accountId, postFilter, pageable);

            // then
            assertThat(posts).hasSize(8);
        }

    }

    @Nested
    @PostScriptSql
    class getPostsByFolder {

        @Test
        void 폴더별_게시글을_조회할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var folderId = new FolderId(1L);
            var postFilter = new PostFilter(null, null, null, null);
            var pageable = Pageable.ofSize(15);

            // when
            var posts = postListQueryService
                .getPostsByFolder(accountId, folderId, postFilter, pageable);

            // then
            assertThat(posts).hasSize(4);
        }
    }

    @Nested
    @PostScriptSql
    class getPostsBySubscription {

        @Test
        void 구독물별_게시글을_조회할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var subscriptionId = new SubscriptionId(1L);
            var postFilter = new PostFilter(null, null, null, null);
            var pageable = Pageable.ofSize(15);

            // when
            var posts = postListQueryService
                .getPostsBySubscription(accountId, subscriptionId, postFilter, pageable);

            // then
            assertThat(posts).hasSize(2);
        }
    }

    @Nested
    @PostScriptSql
    class getBookmarkedPosts {

        @Test
        void 북마크된_게시글을_조회할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var postFilter = new PostFilter(null, null, null, null);
            var pageable = Pageable.ofSize(15);

            postCommand.updateOnlyBookmark(
                PostAggregate.builder()
                    .id(new PostId("20240101000000-1"))
                    .bookmark(Bookmark.MARKED).build(),
                accountId
            );

            postCommand.updateOnlyBookmark(
                PostAggregate.builder()
                    .id(new PostId("20240101010000-1"))
                    .bookmark(Bookmark.MARKED).build(),
                accountId
            );

            // when
            var posts = postListQueryService
                .getBookmarkedPosts(accountId, postFilter, pageable);

            // then
            assertThat(posts).hasSize(2);
        }
    }
}