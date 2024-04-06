package com.flytrap.rssreader.global.properties;

import com.flytrap.rssreader.api.member.domain.Member;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AdminProperties(String code,
                              int memberId,
                              String memberName,
                              String memberEmail,
                              String memberProfile) {

    public Member getMember() {
        return Member.adminOf(memberId, memberName, memberEmail, memberProfile);
    }
}
