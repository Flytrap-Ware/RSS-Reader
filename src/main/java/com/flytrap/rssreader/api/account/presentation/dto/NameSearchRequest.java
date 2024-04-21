package com.flytrap.rssreader.api.account.presentation.dto;

import com.flytrap.rssreader.api.account.presentation.dto.response.AccountSummary;

import java.util.List;

public record NameSearchRequest(String name) {
    public record Result(List<AccountSummary> memberSummary) {
    }
}
