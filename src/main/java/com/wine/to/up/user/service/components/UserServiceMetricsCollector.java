package com.wine.to.up.user.service.components;

import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;
import org.springframework.stereotype.Component;

/**
 * This Class expose methods for recording specific metrics
 * It changes metrics of Micrometer and Prometheus simultaneously
 * Micrometer's metrics exposed at /actuator/prometheus
 * Prometheus' metrics exposed at /metrics-prometheus
 */
@Component
public class UserServiceMetricsCollector extends CommonMetricsCollector {
    public UserServiceMetricsCollector() {
        super("user_service");
    }
}
