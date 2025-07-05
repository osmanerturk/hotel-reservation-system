package com.hotel.notificationservice.kafka;


import com.hotel.common.dto.ReservationNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationConsumer {

    @KafkaListener(topics = "reservation-created",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleReservationCreated(ReservationNotification notification) {
        log.info("📩 Received reservation event: {}", notification);
    }
}
