package com.flytrap.rssreader.api.alert.business.service.dto;


import java.util.Map;

public record AlertParam(
        String folderName,
        String webhookUrl,
        Map<String, String> posts
) {
    
    public static AlertParam create(String folderName, String webhookUrl, Map<String, String> posts) {
        return new AlertParam(folderName, webhookUrl, posts);
    }
}
