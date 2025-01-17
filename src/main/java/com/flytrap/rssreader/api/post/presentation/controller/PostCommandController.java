package com.flytrap.rssreader.api.post.presentation.controller;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.post.business.service.PostCommandService;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.presentation.controller.swagger.PostCommandControllerApi;
import com.flytrap.rssreader.api.post.presentation.dto.response.BookmarkResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostCommandController implements PostCommandControllerApi {

    private final PostCommandService postCommandService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/posts/{postId}/read")
    public ApplicationResponse<Void> unmarkAsOpened(
        @PathVariable String postId,
        @Login AccountCredentials accountCredentials
    ) {
        postCommandService.unmarkAsOpen(new AccountId(accountCredentials.id().value()), new PostId(postId));

        return new ApplicationResponse<>(null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/posts/{postId}/bookmarks")
    public ApplicationResponse<BookmarkResponse> markAsBookmark(
        @PathVariable String postId,
        @Login AccountCredentials accountCredentials
    ) {
        postCommandService
            .markAsBookmark(new AccountId(accountCredentials.id().value()), new PostId(postId));

        return new ApplicationResponse<>(new BookmarkResponse(accountCredentials.id().value(), postId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/posts/{postId}/bookmarks")
    public ApplicationResponse<Void> unmarkAsBookmark(
        @PathVariable String postId,
        @Login AccountCredentials accountCredentials
    ) {
        postCommandService
            .unmarkAsBookmark(new AccountId(accountCredentials.id().value()), new PostId(postId));

        return new ApplicationResponse<>(null);
    }

}
