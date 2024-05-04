package com.flytrap.rssreader.api.folder.presentation.controller.swagger;

import com.flytrap.rssreader.api.account.presentation.dto.response.AccountSummary;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.model.ErrorResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.InviteFolderMemberRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.security.sasl.AuthenticationException;

@Tag(name ="공유 폴더")
public interface SharedFolderUpdateControllerApi {

    @Operation(summary = "폴더에 회원 초대", description = "폴더에 회원을 한명 초대한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountSummary.class))),
        @ApiResponse(responseCode = "400", description = "실패: 이미 초대되어 있을 때",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "실패: 초대 권한이 없을 때",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    ApplicationResponse<AccountSummary> inviteMember(
        @Parameter(description = "회원을 초대할 폴더의 ID") @PathVariable Long folderId,
        @Parameter(description = "현재 로그인한 회원 정보") @Login AccountCredentials loginMember,
        @Parameter(description = "초대할 회원의 ID") @RequestBody InviteFolderMemberRequest request
    ) throws AuthenticationException;

    @Operation(summary = "초대된 폴더에서 떠나기", description = "내가 초대된 폴더에서 스스로 나간다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
    })
    ApplicationResponse<String> leaveFolder(
        @Parameter(description = "떠나고자 하는 폴더의 ID") @PathVariable Long folderId,
        @Parameter(description = "현재 로그인한 회원 정보") @Login AccountCredentials member
    );

    @Operation(summary = "공유 폴더에 포함된 한 회원 추방하기", description = "공유 폴더에 포함된 한 회원 추방한다. 폴더 관리자만 추방할 수 있다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "실패: 폴더에 추가되어있지 않은 멤버 삭제할 때",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "실패: 추방 권한이 없을 때",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    ApplicationResponse<String> deleteMember(
        @Parameter(description = "추방이 일어날 폴더의 ID") @PathVariable Long folderId,
        @Parameter(description = "현재 폴더에서 추방시킬 회원의 ID") @PathVariable Long inviteeId,
        @Parameter(description = "현재 로그인한 회원 정보") @Login AccountCredentials member
    ) throws AuthenticationException;
}
