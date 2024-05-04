package com.flytrap.rssreader.api.account.presentation.controller;

import com.flytrap.rssreader.api.account.business.service.AccountService;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.api.account.presentation.controller.swagger.AccountControllerApi;
import com.flytrap.rssreader.api.account.presentation.dto.NameSearchRequest;
import com.flytrap.rssreader.api.account.presentation.dto.NameSearchResponse;
import com.flytrap.rssreader.api.account.presentation.dto.AccountSummaryResponse;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class AccountController implements AccountControllerApi {

    private final AccountService memberService;

    @GetMapping
    public ApplicationResponse<NameSearchResponse> searchAccountByName(NameSearchRequest nameSearch) {
        List<AccountSummaryResponse> memberSummaries = memberService.get(new AccountName(nameSearch.name()))
                .stream().map(AccountSummaryResponse::from)
                .toList();
        return new ApplicationResponse<>(new NameSearchResponse(memberSummaries));
    }

}
