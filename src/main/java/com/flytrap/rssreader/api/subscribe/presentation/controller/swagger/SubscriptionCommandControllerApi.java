package com.flytrap.rssreader.api.subscribe.presentation.controller.swagger;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.subscribe.presentation.dto.AddSubscriptionRequest;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscriptionResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
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

public interface SubscriptionCommandControllerApi {

    @Operation(summary = "폴더에 구독 추가하기", description = "이미 추가된 폴더에 구독을 새로 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "구독 추가 성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionResponse.class))),
    })
    ApplicationResponse<SubscriptionResponse> addSubscribeToFolder(
        @Parameter(description = "블로그를 추가할 폴더의 ID") @PathVariable Long folderId,
        @Parameter(description = "추가할 블로그의 주소") @Valid @RequestBody AddSubscriptionRequest request,
        @Parameter(description = "현재 로그인한 회원 정보") @Login AccountCredentials accountCredentials);

    @Operation(summary = "폴더에서 구독 제거하기", description = "폴더에 구독되어 있던 구독을 제거한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "구독 제거 성공",  content = @Content(mediaType = "application/json", schema = @Schema())),
    })
    ApplicationResponse<Void> removeSubscriptionToFolder(
        @PathVariable Long folderId,
        @PathVariable Long subscriptionId,
        @Login AccountCredentials accountCredentials
    );
}
