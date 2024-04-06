package com.flytrap.rssreader.api.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class OauthInfo {

    private Long oauthPk;
    private OauthServer oauthServer;
}
