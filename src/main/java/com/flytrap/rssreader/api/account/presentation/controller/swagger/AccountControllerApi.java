package com.flytrap.rssreader.api.account.presentation.controller.swagger;

import com.flytrap.rssreader.api.account.presentation.dto.NameSearchRequest;
import com.flytrap.rssreader.api.account.presentation.dto.NameSearchResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface AccountControllerApi {

    @Operation(summary = "이름으로 Account 검색", description = "등록된 이름으로 Account를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = NameSearchResponse.class))),
    })
    ApplicationResponse<NameSearchResponse> searchAccountByName(
            @Parameter(description = "OAuth 인증 후 반환받은 code") NameSearchRequest nameSearch);
}
