package com.flytrap.rssreader.api.post.presentation.controller.swagger;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.post.presentation.dto.response.PostResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag("Posts")
public interface PostReadControllerApi {

    @Operation(summary = "게시글 불러오기", description = "게시글 하나를 불러온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType="application/json", schema = @Schema(implementation = PostResponse.class))),
    })
    ApplicationResponse<PostResponse> getPost(
            @PathVariable Long postId,
            @Login AccountCredentials accountCredentials);

}
