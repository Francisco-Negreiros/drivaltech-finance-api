package com.drivaltech.finance.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String method = request.getMethod();
        String uri = request.getRequestURI();

        logger.info("[REQUEST] {} {}", method, uri);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {

        Long startTime = (Long) request.getAttribute("startTime");

        if (startTime == null) {
            return;
        }

        long duration = System.currentTimeMillis() - startTime;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        logger.info("[RESPONSE] {} {} | status={} | duration={}ms",
                method, uri, status, duration);
    }
}