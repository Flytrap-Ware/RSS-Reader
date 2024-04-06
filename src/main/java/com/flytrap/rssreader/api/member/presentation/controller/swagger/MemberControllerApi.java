package com.flytrap.rssreader.api.member.presentation.controller.swagger;

import com.flytrap.rssreader.api.member.presentation.dto.NameSearch;
import com.flytrap.rssreader.global.model.ApplicationResponse;

public interface MemberControllerApi {

    // TODO: Swaager 어노테이션 붙여주세요.
    ApplicationResponse<NameSearch.Result> searchMemberByName(NameSearch nameSearch);
}
