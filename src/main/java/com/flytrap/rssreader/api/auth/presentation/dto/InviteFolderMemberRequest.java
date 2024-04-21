package com.flytrap.rssreader.api.auth.presentation.dto;

import jakarta.validation.constraints.Min;

public record InviteFolderMemberRequest(@Min(0) long inviteeId) {

}
