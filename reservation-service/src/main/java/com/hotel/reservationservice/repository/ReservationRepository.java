package com.hotel.reservationservice.repository;

import com.hotel.reservationservice.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.hotelId = :hotelId AND r.roomNumber = :roomNumber AND " +
            "(:checkInDate < r.checkOutDate AND :checkOutDate > r.checkInDate)")
    List<Reservation> findConflictingReservationsWithLock(
            @Param("hotelId") Long hotelId,
            @Param("roomNumber") String roomNumber,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );
}
