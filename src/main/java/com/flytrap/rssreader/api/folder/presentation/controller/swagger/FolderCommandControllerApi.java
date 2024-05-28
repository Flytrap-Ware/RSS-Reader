package com.flytrap.rssreader.api.folder.presentation.controller.swagger;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderUpdateRequest;
import com.flytrap.rssreader.api.folder.presentation.dto.FolderUpdateResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface FolderCommandControllerApi {

    @Operation(summary = "새로운 폴더 추가하기", description = "새로운 폴더를 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "폴더 추가 성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = FolderUpdateResponse.class))),
    })
    ApplicationResponse<FolderUpdateResponse> createNewFolder(
        @Valid @RequestBody FolderUpdateRequest request,
        @Login AccountCredentials accountCredentials);

    @Operation(summary = "폴더 정보 변경하기", description = "폴더의 정보를 변경한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "폴더의 정보를 변경 성공",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = FolderUpdateResponse.class))),
    })
    ApplicationResponse<FolderUpdateResponse> updateFolder(
        @Valid @RequestBody FolderUpdateRequest request,
        @PathVariable Long folderId,
        @Login AccountCredentials accountCredentials);

    @Operation(summary = "폴더 제거하기", description = "폴더를 제거한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "폴더 제거 성공",  content = @Content(mediaType = "application/json")),
    })
    ApplicationResponse<Void> deleteFolder(
        @PathVariable Long folderId,
        @Login AccountCredentials accountCredentials);

}
