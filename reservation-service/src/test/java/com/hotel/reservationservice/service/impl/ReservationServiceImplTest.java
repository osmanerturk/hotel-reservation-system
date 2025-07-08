package com.hotel.reservationservice.service.impl;

import com.hotel.common.dto.ReservationNotification;
import com.hotel.reservationservice.client.HotelServiceClient;
import com.hotel.reservationservice.dto.ReservationRequest;
import com.hotel.reservationservice.dto.ReservationResponse;
import com.hotel.reservationservice.entity.Reservation;
import com.hotel.reservationservice.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationProducer reservationProducer;

    @Mock
    private HotelServiceClient hotelServiceClient;

    @InjectMocks
    private ReservationServiceImpl reservationService;


    @Test
    void createReservation_success() {
        ReservationRequest request = new ReservationRequest();
        request.setHotelId(1L);
        request.setRoomNumber("101");
        request.setGuestName("John Doe");
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));

        when(hotelServiceClient.hotelExists("101", 1L)).thenReturn(true);
        when(reservationRepository.findConflictingReservationsWithLock(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        Reservation savedReservation = Reservation.builder()
                .id(1L)
                .hotelId(request.getHotelId())
                .roomNumber(request.getRoomNumber())
                .guestName(request.getGuestName())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .build();

        when(reservationRepository.save(any())).thenReturn(savedReservation);

        ReservationResponse response = reservationService.createReservation(request);

        assertNotNull(response);
        assertEquals(request.getGuestName(), response.getGuestName());
        verify(reservationProducer).sendReservationCreatedEvent(any(ReservationNotification.class));
    }

    @Test
    void createReservation_conflict_throwsException() {
        ReservationRequest request = new ReservationRequest();
        request.setHotelId(1L);
        request.setRoomNumber("101");
        request.setGuestName("Jane Doe");
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));

        when(hotelServiceClient.hotelExists("101", 1L)).thenReturn(true);
        when(reservationRepository.findConflictingReservationsWithLock(any(), any(), any(), any()))
                .thenReturn(List.of(new Reservation()));

        assertThrows(RuntimeException.class, () -> reservationService.createReservation(request));
    }

    @Test
    void getReservation_found() {
        Reservation reservation = Reservation.builder().id(1L).guestName("Alice").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ReservationResponse response = reservationService.getReservation(1L);

        assertNotNull(response);
        assertEquals("Alice", response.getGuestName());
    }

    @Test
    void getReservation_notFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reservationService.getReservation(99L));
    }
}
