package com.flytrap.rssreader.api.auth.presentation.controller.swagger;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerApi {

    @Operation(summary = "로그인 및 회원가입", description = "OAuth 인증 후 반환받은 code로 로그인할 수 있다. 처음 로그인한 회원은 회원 가입과 로그인을 같이 수행한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
    })
    ApplicationResponse<LoginResponse> login(
        @Parameter(description = "OAuth 인증 후 반환받은 code") @RequestBody LoginRequest request,
        @Parameter(description = "로그인시 세션 생성에 사용할 HttpSession") HttpSession session);

    @Operation(summary = "로그아웃", description = "세션에서 요청 회원의 접속 상태 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공"),
    })
    ApplicationResponse<Void> logout(HttpSession session);
}
