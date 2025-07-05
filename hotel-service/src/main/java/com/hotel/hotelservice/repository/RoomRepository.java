package com.hotel.hotelservice.repository;


import com.hotel.hotelservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    Optional<Room> findByRoomNumberAndHotelId(String roomNumber, Long hotelId);
}
