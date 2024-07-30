package com.deyvidsalvatore.web.gestaomasterx.config;

import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class TimezoneConfig {
    
    private static final Logger LOG = LoggerFactory.getLogger(TimezoneConfig.class);
    
    @PostConstruct
    void configureLocaleAndTimezone() {
        // Get and set the default locale
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(defaultLocale);
        LOG.info("Default locale set to: {}", defaultLocale.toString());
        
        // Get and set the default time zone
        TimeZone defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(defaultTimeZone);
        LOG.info("Default time zone set to: {}", defaultTimeZone.getID());
    }
}
