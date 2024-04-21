package com.flytrap.rssreader.api.alert.presentation.controller;

import com.flytrap.rssreader.api.alert.business.service.AlertService;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.folder.business.service.FolderVerifyService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertListResponse;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertRequest;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionAccount;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class AlertController implements AlertControllerApi {

    private final AlertService alertService;
    private final FolderVerifyService folderVerifyService;

    @GetMapping("/{folderId}/alerts")
    public ApplicationResponse<AlertListResponse> getAlerts(
        @PathVariable Long folderId,
        @Login SessionAccount member
    ) {
        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id().value());
        List<Alert> alerts = alertService.getAlertListByFolder(folderId);

        return new ApplicationResponse<>(AlertListResponse.from(alerts));
    }

    @PostMapping("/{folderId}/alerts")
    public ApplicationResponse<AlertResponse> registerAlert(
        @PathVariable Long folderId,
        @Valid @RequestBody AlertRequest request,
        @Login SessionAccount member) {

        Folder verifiedFolder = folderVerifyService.getVerifiedAccessableFolder(folderId, member.id().value());
        Alert alert = alertService.registerAlert(verifiedFolder.getId(), member.id().value(), request.webhookUrl());
        return new ApplicationResponse<>(AlertResponse.from(alert));
    }

    @DeleteMapping("/{folderId}/alerts/{alertId}")
    public ApplicationResponse<String> removeAlert(
        @PathVariable Long folderId,
        @PathVariable Long alertId,
        @Login SessionAccount member) {

        folderVerifyService.getVerifiedAccessableFolder(folderId, member.id().value());
        alertService.removeAlert(alertId);

        return new ApplicationResponse<>("알람이 삭제되었습니다. ID = " + alertId);
    }

}
