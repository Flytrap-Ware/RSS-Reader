package com.flytrap.rssreader.api.folder.presentation.controller.swagger;

import com.flytrap.rssreader.api.folder.presentation.dto.Folders;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

public interface FolderReadControllerApi {

    @Operation(summary = "폴더 목록 불러오기", description = "현재 회원이 생성한 폴더 목록을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Folders.class))),
    })
    ApplicationResponse<Folders> getFolders(
            @Parameter(description = "현재 로그인한 회원 정보") @Login AccountSession member
    );

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<SubscribeRequest.ResponseList> read(
            @PathVariable Long folderId,
            @Login AccountSession member);
}
