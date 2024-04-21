package com.flytrap.rssreader.api.account.presentation.controller;

import com.flytrap.rssreader.api.account.business.service.AccountService;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.api.account.presentation.controller.swagger.AccountControllerApi;
import com.flytrap.rssreader.api.account.presentation.dto.NameSearchRequest;
import com.flytrap.rssreader.api.account.presentation.dto.response.AccountSummary;
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
    public ApplicationResponse<NameSearchRequest.Result> searchAccountByName(NameSearchRequest nameSearch) {
        List<AccountSummary> memberSummaries = memberService.get(new AccountName(nameSearch.name()))
                .stream().map(AccountSummary::from)
                .toList();
        return new ApplicationResponse<>(new NameSearchRequest.Result(memberSummaries));
    }

}
