package com.hotel.hotelservice.service;


import com.hotel.common.dto.RoomDTO;

import java.util.List;

public interface RoomService {
    RoomDTO createRoom(RoomDTO roomDTO);

    RoomDTO getRoom(Long id);

    RoomDTO getRoomWithHotelIdAndRoomNumber(String roomNumber, Long hotelId);

    List<RoomDTO> getRoomsByHotelId(Long hotelId);
    RoomDTO updateRoom(Long id, RoomDTO roomDTO);
    void deleteRoom(Long id);
}
