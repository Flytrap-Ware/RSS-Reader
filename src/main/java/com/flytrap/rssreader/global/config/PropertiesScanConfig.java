package com.flytrap.rssreader.global.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan("com.flytrap.rssreader.global.properties")
public class PropertiesScanConfig {
}
