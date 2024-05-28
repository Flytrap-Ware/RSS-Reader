package com.flytrap.rssreader.api.alert.presentation.controller;

import com.flytrap.rssreader.api.alert.business.service.AlertQueryService;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.presentation.controller.swagger.AlertQueryControllerApi;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertListResponse;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlertQueryController implements AlertQueryControllerApi {

    private final AlertQueryService alertQueryService;

    @GetMapping("/api/folders/{folderId}/alerts")
    public ApplicationResponse<AlertListResponse> getAlertsByFolder(
        @PathVariable Long folderId,
        @Login AccountCredentials accountCredentials
    ) {
        List<Alert> alerts = alertQueryService
            .getAlertsByFolder(new FolderId(folderId), accountCredentials.id());

        return new ApplicationResponse<>(AlertListResponse.from(alerts));
    }
}
