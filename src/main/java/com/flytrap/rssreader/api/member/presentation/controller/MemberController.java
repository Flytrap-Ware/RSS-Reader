package com.flytrap.rssreader.api.member.presentation.controller;

import com.flytrap.rssreader.api.member.business.service.MemberService;
import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.api.member.presentation.controller.swagger.MemberControllerApi;
import com.flytrap.rssreader.api.member.presentation.dto.NameSearch;
import com.flytrap.rssreader.api.member.presentation.dto.response.MemberSummary;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberControllerApi {

    private final MemberService memberService;

    @GetMapping
    public ApplicationResponse<NameSearch.Result> searchMemberByName(NameSearch nameSearch) {
        List<Member> results = memberService.findByName(nameSearch.name());

        List<MemberSummary> memberSummaries = results.stream().map(MemberSummary::from).toList();
        return new ApplicationResponse<>(new NameSearch.Result(memberSummaries));
    }

}
