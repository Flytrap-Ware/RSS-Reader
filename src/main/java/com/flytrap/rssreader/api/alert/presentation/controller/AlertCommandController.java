package com.flytrap.rssreader.api.alert.presentation.controller;

import com.flytrap.rssreader.api.alert.business.service.AlertCommandService;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertId;
import com.flytrap.rssreader.api.alert.presentation.controller.swagger.AlertCommandControllerApi;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertRequest;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlertCommandController implements AlertCommandControllerApi {

    private final AlertCommandService alertCommandService;

    @PostMapping("/api/folders/{folderId}/alerts")
    public ApplicationResponse<AlertResponse> registerAlert(
        @PathVariable Long folderId,
        @Valid @RequestBody AlertRequest request,
        @Login AccountCredentials accountCredentials
    ) {
        Alert alert = alertCommandService.registerAlert(
            new FolderId(folderId), accountCredentials.id(),  request.webhookUrl()
        );

        return new ApplicationResponse<>(AlertResponse.from(alert));
    }

    @DeleteMapping("/api/folders/{folderId}/alerts/{alertId}")
    public ApplicationResponse<String> removeAlert(
        @PathVariable Long folderId,
        @PathVariable Long alertId,
        @Login AccountCredentials accountCredentials)
    {
        alertCommandService.removeAlert(
            new FolderId(folderId), accountCredentials.id(), new AlertId(alertId)
        );

        return new ApplicationResponse<>("알람이 삭제되었습니다. ID = " + alertId);
    }

}
