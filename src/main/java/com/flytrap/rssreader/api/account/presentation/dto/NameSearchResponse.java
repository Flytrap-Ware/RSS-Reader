package com.flytrap.rssreader.api.account.presentation.dto;

import java.util.List;

public record NameSearchResponse(List<AccountSummaryResponse> memberSummary) {
}
