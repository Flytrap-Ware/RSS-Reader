package com.flytrap.rssreader.api.member.presentation.dto;

import com.flytrap.rssreader.api.member.presentation.dto.response.MemberSummary;

import java.util.List;

public record NameSearch(String name) {

    public record Result(List<MemberSummary> memberSummary) {
    }
}
