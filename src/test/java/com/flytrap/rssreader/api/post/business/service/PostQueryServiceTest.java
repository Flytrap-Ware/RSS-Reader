package com.flytrap.rssreader.api.post.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.flytrap.rssreader.CustomServiceTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.post.PostScriptSql;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostQuery;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@CustomServiceTest
class PostQueryServiceTest {

    @Autowired
    PostQueryService postQueryService;

    @Autowired
    PostQuery postQuery;

    @Nested
    @PostScriptSql
    class ViewPost {

        @Test
        void 게시글_정보를_불러올_수_있다() {
            // given
            var accountId = new AccountId(1L);
            var postId = new PostId("20240101000000-1");

            // when
            var result = postQueryService.viewPost(accountId, postId);

            //then
            assertThat(result.getId()).isEqualTo(postId);
        }

        @Test
        void 존재하지_않는_게시글을_불러오면_예외가_발생한다() {
            // given
            var accountId = new AccountId(1L);
            var postId = new PostId("00000000000000-999");

            // when
            Executable viewPostExecutable = ()
                -> postQueryService.viewPost(accountId, postId);

            //then
            assertThrows(NoSuchDomainException.class, viewPostExecutable);
        }

    }
}