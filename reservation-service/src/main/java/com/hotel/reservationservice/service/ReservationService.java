package com.hotel.reservationservice.service;


import com.hotel.reservationservice.dto.ReservationRequest;
import com.hotel.reservationservice.dto.ReservationResponse;

import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request);
    ReservationResponse getReservation(Long id);
    List<ReservationResponse> getAllReservations();

    List<ReservationResponse> getReservationsByUsername(String username);
}

