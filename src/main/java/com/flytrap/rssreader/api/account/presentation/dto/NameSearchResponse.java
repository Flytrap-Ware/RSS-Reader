package com.flytrap.rssreader.api.account.presentation.dto;

import com.flytrap.rssreader.api.account.presentation.dto.response.AccountSummary;

import java.util.List;

public record NameSearchResponse(List<AccountSummary> memberSummary) {
}
