package com.hotel.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Value("${services.hotel}")
    private String hotelServiceUrl;

    @Value("${services.reservation}")
    private String reservationServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("hotel-service", r -> r.path("/api/hotels/**","/api/rooms/**")
                        .uri(hotelServiceUrl))
                .route("reservation-service", r -> r.path("/api/reservations/**")
                        .uri(reservationServiceUrl))
                .build();
    }
}