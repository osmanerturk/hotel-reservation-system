package com.hotel.hotelservice.controller;

import com.hotel.common.dto.RoomDTO;
import com.hotel.hotelservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDTO> create(@RequestBody @Valid RoomDTO roomDTO) {
        RoomDTO createdRoom = roomService.createRoom(roomDTO);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getById(@PathVariable Long id) {
        RoomDTO room = roomService.getRoom(id);
        return new ResponseEntity<>(room,HttpStatus.OK);
    }


    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDTO>> getByHotel(@PathVariable Long hotelId) {
        List<RoomDTO> roomsByHotelId = roomService.getRoomsByHotelId(hotelId);
        return new ResponseEntity<>(roomsByHotelId,HttpStatus.OK);
    }

    @GetMapping("/{roomNumber}/{hotelId}")
    public ResponseEntity<RoomDTO> getById(@PathVariable String roomNumber, @PathVariable Long hotelId) {
        RoomDTO roomWithHotelIdAndRoomNumber = roomService.getRoomWithHotelIdAndRoomNumber(roomNumber, hotelId);
        return new ResponseEntity<>(roomWithHotelIdAndRoomNumber,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> update(@PathVariable Long id, @RequestBody @Valid RoomDTO roomDTO) {
        RoomDTO updatedRoom = roomService.updateRoom(id, roomDTO);
        return new ResponseEntity<>(updatedRoom,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}

