package com.flytrap.rssreader.api.post.presentation.controller;

import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.post.business.service.PostListReadService;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.presentation.controller.swagger.PostListReadControllerApi;
import com.flytrap.rssreader.api.post.presentation.dto.response.PostResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostListReadController implements PostListReadControllerApi {

    private final PostListReadService postListReadService;
    private final FolderVerifyService folderVerifyService;

    @GetMapping("/subscribes/{subscribeId}/posts")
    public ApplicationResponse<PostResponse.PostListResponse> getPostsBySubscribe(
            @PathVariable Long subscribeId,
            PostFilter postFilter, // TODO: filter 가 request 부터 repository까지 계속 전달됨
            @PageableDefault(page = 0, size = 15) Pageable pageable, // TODO: pageable 도 마찬가지. service 에서 만들면 됨
            @Login SessionMember member) {

        List<PostResponse> posts = postListReadService.getPostsBySubscribe(member, subscribeId, postFilter, pageable)
                .stream()
                .map(PostResponse::from)
                .toList();

        return new ApplicationResponse<>(
                new PostResponse.PostListResponse(posts));
    }

    @GetMapping("/folders/{folderId}/posts")
    public ApplicationResponse<PostResponse.PostListResponse> getPostsByFolder(
            @PathVariable Long folderId,
            PostFilter postFilter, // TODO: filter 가 request 부터 repository까지 계속 전달됨
            @PageableDefault(page = 0, size = 15) Pageable pageable,
            @Login SessionMember member) {

        Folder verifyFolder = folderVerifyService.getVerifiedOwnedFolder(folderId, member.id());

        List<PostResponse> posts = postListReadService.getPostsByFolder(member, verifyFolder, postFilter, pageable)
                .stream()
                .map(PostResponse::from)
                .toList();

        return new ApplicationResponse<>(
                new PostResponse.PostListResponse(posts));
    }

    @GetMapping("/posts")
    public ApplicationResponse<PostResponse.PostListResponse> getPostsByMember(
            PostFilter postFilter, // TODO: filter 가 request 부터 repository까지 계속 전달됨
            @PageableDefault(page = 0, size = 15) Pageable pageable,
            @Login SessionMember member) {

        List<PostResponse> posts = postListReadService.getPostsByMember(member, postFilter, pageable)
                .stream()
                .map(PostResponse::from)
                .toList();

        return new ApplicationResponse<>(
                new PostResponse.PostListResponse(posts));
    }
}
