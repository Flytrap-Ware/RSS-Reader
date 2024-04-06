package com.flytrap.rssreader.api.auth.infrastructure.external.dto;

public record UserEmailResource(String email,
                                boolean primary,
                                boolean verified) {

}
