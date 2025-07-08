package com.hotel.reservationservice.service.impl;

import com.hotel.common.dto.ReservationNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "reservation-created";

    public void sendReservationCreatedEvent(ReservationNotification event) {
        kafkaTemplate.send(TOPIC, event);
    }
}

