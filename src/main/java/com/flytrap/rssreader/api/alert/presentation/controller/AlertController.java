package com.flytrap.rssreader.api.alert.presentation.controller;

import com.flytrap.rssreader.api.alert.business.service.AlertService;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.presentation.controller.swagger.AlertControllerApi;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertListResponse;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertRequest;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlertController implements AlertControllerApi {

    private final AlertService alertService;
    private final FolderVerifyService folderVerifyService;

    @GetMapping("/api/folders/{folderId}/alerts")
    public ApplicationResponse<AlertListResponse> getAlertsByFolder(
        @PathVariable Long folderId,
        @Login AccountCredentials accountCredentials
    ) {
        List<Alert> alerts = alertService
            .getAlertsByFolder(new FolderId(folderId), accountCredentials.id());

        return new ApplicationResponse<>(AlertListResponse.from(alerts));
    }

    @PostMapping("/api/folders/{folderId}/alerts")
    public ApplicationResponse<AlertResponse> registerAlert(
        @PathVariable Long folderId,
        @Valid @RequestBody AlertRequest request,
        @Login AccountCredentials accountCredentials
    ) {
        Alert alert = alertService.registerAlert(
            new FolderId(folderId), accountCredentials.id(),  request.webhookUrl()
        );

        return new ApplicationResponse<>(AlertResponse.from(alert));
    }

    @DeleteMapping("/api/folders/{folderId}/alerts/{alertId}")
    public ApplicationResponse<String> removeAlert(
        @PathVariable Long folderId,
        @PathVariable Long alertId,
        @Login AccountCredentials member) {

        folderVerifyService.getVerifiedAccessableFolder(folderId, member.id().value());
        alertService.removeAlert(alertId);

        return new ApplicationResponse<>("알람이 삭제되었습니다. ID = " + alertId);
    }

}
