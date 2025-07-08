package com.hotel.reservationservice.controller;

import com.hotel.reservationservice.dto.ReservationRequest;
import com.hotel.reservationservice.dto.ReservationResponse;
import com.hotel.reservationservice.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid ReservationRequest request) {
        ReservationResponse createdReservation = reservationService.createReservation(request);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getById(@PathVariable("id") Long id) {
        ReservationResponse reservation = reservationService.getReservation(id);
        return new ResponseEntity<>(reservation,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll() {
        List<ReservationResponse> allReservations = reservationService.getAllReservations();
        return new ResponseEntity<>(allReservations,HttpStatus.OK);
    }


    @GetMapping("/user")
    public ResponseEntity<List<ReservationResponse>> getUserReservations() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ReservationResponse> reservationsByUsername = reservationService.getReservationsByUsername(username);
        return new ResponseEntity<>(reservationsByUsername,HttpStatus.OK);
    }

}
