package com.hotel.reservationservice.service.impl;

import com.hotel.common.dto.ReservationNotification;
import com.hotel.reservationservice.client.HotelServiceClient;
import com.hotel.reservationservice.dto.ReservationRequest;
import com.hotel.reservationservice.dto.ReservationResponse;
import com.hotel.reservationservice.entity.Reservation;
import com.hotel.reservationservice.repository.ReservationRepository;
import com.hotel.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationProducer reservationProducer;
    private final HotelServiceClient hotelServiceClient;

    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {

        if (!hotelServiceClient.hotelExists(request.getRoomNumber(),request.getHotelId())) {
            throw new IllegalArgumentException("Hotel not found");
        }
        boolean hasConflict = reservationRepository
                .findConflictingReservationsWithLock(
                        request.getHotelId(),
                        request.getRoomNumber(),
                        request.getCheckInDate(),
                        request.getCheckOutDate()
                )
                .stream()
                .findAny()
                .isPresent();

        if (hasConflict) {
            throw new RuntimeException("Selected room is already reserved for given dates.");
        }

        Reservation reservation = Reservation.builder()
                .hotelId(request.getHotelId())
                .roomNumber(request.getRoomNumber())
                .guestName(request.getGuestName())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .build();

        Reservation saved = reservationRepository.save(reservation);

        this.sendReservationEvent(reservation);

        return toResponse(saved);
    }

    public void sendReservationEvent(Reservation reservation) {
        ReservationNotification event = new ReservationNotification(
                reservation.getId(),
                reservation.getHotelId(),
                reservation.getRoomNumber(),
                reservation.getGuestName(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getCreatedAt()
        );

        reservationProducer.sendReservationCreatedEvent(event);
    }

    @Override
    public ReservationResponse getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return toResponse(reservation);
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        BeanUtils.copyProperties(reservation, response);
        return response;
    }

    @Override
    public List<ReservationResponse> getReservationsByUsername(String username) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getGuestName().equals(username))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
