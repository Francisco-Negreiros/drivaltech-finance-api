package com.drivaltech.finance.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUESTS = 100;
    private static final long TIME_WINDOW = 60_000; // 1 minuto

    private final Map<String, RequestInfo> requests = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String ip = getClientIp(request);
        long currentTime = System.currentTimeMillis();

        RequestInfo info = requests.getOrDefault(ip, new RequestInfo(0, currentTime));

        if (currentTime - info.timestamp > TIME_WINDOW) {
            info = new RequestInfo(0, currentTime);
        }

        info.count++;

        if (info.count > MAX_REQUESTS) {
            response.setStatus(429);
            response.setContentType("application/json");
            try {
                response.getWriter().write("""
        {
            "status": 429,
            "error": "Too Many Requests",
            "message": "Rate limit exceeded. Try again later."
        }
    """);
            } catch (IOException e) {
                log.error("Error writing rate limit response", e);
            }
            return false;
        }

        requests.put(ip, info);

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static class RequestInfo {
        int count;
        long timestamp;

        public RequestInfo(int count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}