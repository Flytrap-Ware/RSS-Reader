package com.flytrap.rssreader.api.post.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.flytrap.rssreader.CustomServiceTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.post.PostScriptSql;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostQuery;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class PostCommandServiceTest {

    @Autowired
    PostCommandService postCommandService;

    @Autowired
    PostQuery postQuery;

    @Nested
    @PostScriptSql
    class UnmarkAsOpen {

        @Test
        void 게시글_읽음_상태를_취소할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var postId = new PostId(1L);

            // when
            postCommandService.unmarkAsOpen(accountId, postId);
            var unOpenedPost = postQuery.read(postId, accountId).get();

            // then
            assertThat(unOpenedPost.getOpen()).isEqualTo(Open.UNMARKED);
        }

        @Test
        void 존재하지_않는_게시글을_조작할_경우_예외를_던진다() {
            // given
            var accountId = new AccountId(1L);
            var postId = new PostId(999L);

            // when
            Executable postExecutable = () ->
                postCommandService.unmarkAsOpen(accountId, postId);

            // then
            assertThrows(NoSuchDomainException.class, postExecutable);
        }

    }

    @Nested
    @PostScriptSql
    class MarkAsBookmark {

        @Test
        void 북마크를_추가할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var postId = new PostId(1L);

            // when
            postCommandService.markAsBookmark(accountId, postId);
            var bookmarkedPost = postQuery.read(postId, accountId).get();

            // then
            assertThat(bookmarkedPost.getBookmark()).isEqualTo(Bookmark.MARKED);
        }

        @Test
        void 존재하지_않는_게시글을_조작할_경우_예외를_던진다() {
            // given
            var accountId = new AccountId(1L);
            var postId = new PostId(999L);

            // when
            Executable postExecutable = () ->
                postCommandService.markAsBookmark(accountId, postId);

            // then
            assertThrows(NoSuchDomainException.class, postExecutable);
        }
    }

    @Nested
    @PostScriptSql
    class UnmarkAsBookmark {

        @Test
        void 북마크를_취소할_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var postId = new PostId(1L);

            // when
            postCommandService.unmarkAsBookmark(accountId, postId);
            var bookmarkedPost = postQuery.read(postId, accountId).get();

            // then
            assertThat(bookmarkedPost.getBookmark()).isEqualTo(Bookmark.UNMARKED);
        }

        @Test
        void 존재하지_않는_게시글을_조작할_경우_예외를_던진다() {
            // given
            var accountId = new AccountId(1L);
            var postId = new PostId(999L);

            // when
            Executable postExecutable = () ->
                postCommandService.unmarkAsBookmark(accountId, postId);

            // then
            assertThrows(NoSuchDomainException.class, postExecutable);
        }
    }
}