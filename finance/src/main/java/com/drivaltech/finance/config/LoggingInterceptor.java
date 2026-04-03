package com.drivaltech.finance.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import org.slf4j.MDC;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        String username = getUsername();
        MDC.put("username", username);

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        MDC.put("ip", ip);

        log.info("[REQUEST] {} {}", request.getMethod(), request.getRequestURI());

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

        long duration = (startTime != null)
                ? System.currentTimeMillis() - startTime
                : 0;

        String correlationId = MDC.get("correlationId");

        log.info("[RESPONSE] {} {} | status={} | duration={}ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration);

        if (ex != null) {
            log.error("[ERROR] [{}] {} {}",
                    correlationId,
                    request.getMethod(),
                    request.getRequestURI(),
                    ex);
        }

        MDC.clear();
    }
    private String getUsername() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }

        return authentication.getName();
    }
}