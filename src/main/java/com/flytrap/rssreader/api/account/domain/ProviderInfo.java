package com.flytrap.rssreader.api.account.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class ProviderInfo {

    private Long providerKey;
    private AuthProvider authProvider;
}
