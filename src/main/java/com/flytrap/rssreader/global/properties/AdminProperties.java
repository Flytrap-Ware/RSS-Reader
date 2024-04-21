package com.flytrap.rssreader.global.properties;

import com.flytrap.rssreader.api.account.domain.Account;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AdminProperties(String code,
                              int memberId,
                              String memberName,
                              String memberEmail,
                              String memberProfile) {

    public Account getMember() {
        return Account.adminOf(memberId, memberName, memberEmail, memberProfile);
    }
}
