package com.flytrap.rssreader.api.alert.presentation.controller.swagger;

import com.flytrap.rssreader.api.alert.presentation.dto.AlertListResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

public interface AlertQueryControllerApi {

    @Operation(summary = "폴더별 웹 훅 알림 목록 불러오기", description = "폴더별 웹 훅 알림 목록을 불러온다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertListResponse.class))),
    })
    ApplicationResponse<AlertListResponse> getAlertsByFolder(
        @Parameter(description = "웹 훅 알림 목록을 조회할 Folder의 Id") @PathVariable Long folderId,
        @Parameter(description = "현재 로그인한 회원 정보") @Login AccountCredentials accountCredentials);

}
