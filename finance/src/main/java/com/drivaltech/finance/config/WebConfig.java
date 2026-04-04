package com.drivaltech.finance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;

    @Autowired
    private LoggingInterceptor loggingInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor);
        registry.addInterceptor(loggingInterceptor);

    }

    public WebConfig(
            LoggingInterceptor loggingInterceptor,
            RateLimitInterceptor rateLimitInterceptor
    ) {
        this.loggingInterceptor = loggingInterceptor;
        this.rateLimitInterceptor = rateLimitInterceptor;
    }
}