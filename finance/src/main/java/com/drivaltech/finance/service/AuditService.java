package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.AuditLog;
import com.drivaltech.finance.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void log(
            UUID userId,
            String action,
            String resource,
            UUID resourceId,
            String ip
    ) {

        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setResource(resource);
        log.setResourceId(resourceId);
        log.setIp(ip);
        log.setTimestamp(LocalDateTime.now());

        repository.save(log);
    }
}
