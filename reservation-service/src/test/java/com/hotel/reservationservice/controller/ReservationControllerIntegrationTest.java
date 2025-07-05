package com.hotel.reservationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.reservationservice.client.HotelServiceClient;
import com.hotel.reservationservice.dto.ReservationRequest;
import com.hotel.reservationservice.entity.Reservation;
import com.hotel.reservationservice.repository.ReservationRepository;
import com.hotel.reservationservice.service.impl.ReservationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ReservationControllerIntegrationTest.MockBeansConfig.class)
public class ReservationControllerIntegrationTest {

    @TestConfiguration
    static class MockBeansConfig {
        @Bean
        public ReservationProducer reservationProducer() {
            return Mockito.mock(ReservationProducer.class);
        }

        @Bean
        public HotelServiceClient hotelServiceClient() {
            return Mockito.mock(HotelServiceClient.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HotelServiceClient hotelServiceClient;

    @BeforeEach
    void cleanUp() {
        reservationRepository.deleteAll();
    }

    @Test
    void createReservation_shouldReturnCreatedReservation() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setHotelId(1L);
        request.setRoomNumber("101");
        request.setGuestName("Alice");
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));

        when(hotelServiceClient.hotelExists("101", 1L)).thenReturn(true);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("Alice"));
    }

    @Test
    void getReservation_shouldReturnReservation() throws Exception {
        Reservation reservation = Reservation.builder()
                .hotelId(1L)
                .roomNumber("102")
                .guestName("Bob")
                .checkInDate(LocalDate.now().plusDays(5))
                .checkOutDate(LocalDate.now().plusDays(7))
                .build();

        reservation = reservationRepository.save(reservation);

        mockMvc.perform(get("/api/reservations/" + reservation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("Bob"));
    }
}
