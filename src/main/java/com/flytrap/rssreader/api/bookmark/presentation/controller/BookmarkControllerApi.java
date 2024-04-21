package com.flytrap.rssreader.api.bookmark.presentation.controller;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import com.flytrap.rssreader.api.bookmark.presentation.dto.BookmarkRequest;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name ="북마크")
public interface BookmarkControllerApi {

    @Operation(summary = "북마크 추가", description = "게시글 하나를 북마크에 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkRequest.Response.class))),
    })
    ApplicationResponse<BookmarkRequest.Response> addBookmark(
        @Parameter(description = "북마크에 추가할 게시글 ID") @PathVariable Long postId,
        @Parameter(description = "현재 로그인한 회원 정보") @Login AccountSession member
    );

    @Operation(summary = "북마크 제거", description = "이미 추가된 북마크 하나를 북마크에서 제거한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
    })
    ApplicationResponse<String> removeBookmark(
        @Parameter(description = "북마크를 제거할 게시글 ID") @PathVariable Long postId,
        @Parameter(description = "현재 로그인한 회원 정보") @Login AccountSession member
    );
}
