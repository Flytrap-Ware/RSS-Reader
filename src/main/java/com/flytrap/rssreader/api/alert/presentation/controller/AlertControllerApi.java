package com.flytrap.rssreader.api.alert.presentation.controller;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertRequest;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface AlertControllerApi {

    @Operation(summary = "알림 웹 훅 등록하기", description = "알림을 보낼 웹 훅 URL을 등록한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertResponse.class))),
    })
    ApplicationResponse<AlertResponse> registerAlert(
        @Parameter(description = "웹 훅을 등록할 Folder의 Id") @PathVariable Long folderId,
        @Parameter(description = "등록할 웹 훅 URL") @Valid @RequestBody AlertRequest request,
        @Parameter(description = "현재 로그인한 회원 정보") @Login SessionMember member);

    @Operation(summary = "알림 웹 훅 삭제하기", description = "등록된 알림 웹 훅을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
    })
    ApplicationResponse<String> removeAlert(
        @Parameter(description = "웹 훅을 삭제할 Folder의 Id") @PathVariable Long folderId,
        @Parameter(description = "삭제할 웹 훅의 Id") @PathVariable Long alertId,
        @Parameter(description = "현재 로그인한 회원 정보") @Login SessionMember member);

}
