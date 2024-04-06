package com.flytrap.rssreader.api.alert.business.event.subscribe;

import java.util.Map;

public record SubscribeEvent(Long subscribeId, Map<String, String> posts) {}
