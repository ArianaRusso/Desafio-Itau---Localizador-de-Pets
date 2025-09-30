package com.itau.localizador_pets.infrastructure.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class PositionStackHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            return Health.up().withDetail("PositionStack", "Available").build();
        } catch (Exception e) {
            return Health.down().withDetail("PositionStack", "Unavailable").build();
        }
    }
}
