package com.flytrap.rssreader.api.post.presentation.controller;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.post.business.service.PostQueryService;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.presentation.controller.swagger.PostQueryControllerApi;
import com.flytrap.rssreader.api.post.presentation.dto.response.PostResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostQueryController implements PostQueryControllerApi {

    private final PostQueryService postQueryService;

    @GetMapping("/api/posts/{postId}")
    public ApplicationResponse<PostResponse> getPost(
        @PathVariable String postId,
        @Login AccountCredentials accountCredentials) {

        PostResponse response = PostResponse.from(
            postQueryService.viewPost(new AccountId(accountCredentials.id().value()), new PostId(postId)));
        return new ApplicationResponse<>(response);
    }
}
