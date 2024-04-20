package com.flytrap.rssreader.api.post.presentation.controller.swagger;

import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.presentation.dto.response.PostResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "게시글 목록 불러오기")
public interface PostListReadControllerApi {

    @Operation(summary = "블로그 게시글 목록 불러오기", description = "구독한 하나의 블로그에 게시된 게시글 목록을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.PostListResponse.class))),
    })
    ApplicationResponse<PostResponse.PostListResponse> getPostsBySubscribe(
            @Parameter(description = "구독한 블로그 ID") @PathVariable Long subscribeId,
            @Parameter(description = "검색용 필터(read: 게시글 열람 여부, start: 검색할 범위의 첫 번째 날짜, end: 검색할 범위의 마지막 날짜, keyword: 검색어)") PostFilter postFilter,
            @Parameter(description = "페이지네이션 용 필터(page: 불러올 페이지 0부터 시작, size: 한번에 불러올 게시글 수)") @PageableDefault(page = 0, size = 15) Pageable pageable,
            @Parameter(description = "현재 로그인한 회원 정보") @Login AccountSession member);

    @Operation(summary = "폴더 게시글 목록 불러오기", description = "폴더에 포한된 모든 블로그들의 게시글 목록을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.PostListResponse.class))),
    })
    ApplicationResponse<PostResponse.PostListResponse> getPostsByFolder(
            @Parameter(description = "폴더 ID") @PathVariable Long folderId,
            @Parameter(description = "검색용 필터(read: 게시글 열람 여부, start: 검색할 범위의 첫 번째 날짜, end: 검색할 범위의 마지막 날짜, keyword: 검색어)") PostFilter postFilter,
            @Parameter(description = "페이지네이션 용 필터(page: 불러올 페이지 0부터 시작, size: 한번에 불러올 게시글 수)") @PageableDefault(page = 0, size = 15) Pageable pageable,
            @Parameter(description = "현재 로그인한 회원 정보") @Login AccountSession member);

    @Operation(summary = "전체 게시글 목록 불러오기", description = "회원이 구독한 모든 블로그의 게시글 목록을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.PostListResponse.class))),
    })
    ApplicationResponse<PostResponse.PostListResponse> getPostsByMember(
            @Parameter(description = "검색용 필터(read: 게시글 열람 여부, start: 검색할 범위의 첫 번째 날짜, end: 검색할 범위의 마지막 날짜, keyword: 검색어)") PostFilter postFilter,
            @Parameter(description = "페이지네이션 용 필터(page: 불러올 페이지 0부터 시작, size: 한번에 불러올 게시글 수)") @PageableDefault(page = 0, size = 15) Pageable pageable,
            @Parameter(description = "현재 로그인한 회원 정보") @Login AccountSession member);

}
