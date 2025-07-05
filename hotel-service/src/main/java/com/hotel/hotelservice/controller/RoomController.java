package com.hotel.hotelservice.controller;

import com.hotel.common.dto.RoomDTO;
import com.hotel.hotelservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public RoomDTO create(@RequestBody @Valid RoomDTO roomDTO) {
        return roomService.createRoom(roomDTO);
    }

    @GetMapping("/{id}")
    public RoomDTO getById(@PathVariable Long id) {
        return roomService.getRoom(id);
    }


    @GetMapping("/hotel/{hotelId}")
    public List<RoomDTO> getByHotel(@PathVariable Long hotelId) {
        return roomService.getRoomsByHotelId(hotelId);
    }

    @GetMapping("/{roomNumber}/{hotelId}")
    public RoomDTO getById(@PathVariable String roomNumber,@PathVariable Long hotelId) {
        return roomService.getRoomWithHotelIdAndRoomNumber(roomNumber, hotelId);
    }

    @PutMapping("/{id}")
    public RoomDTO update(@PathVariable Long id, @RequestBody @Valid RoomDTO roomDTO) {
        return roomService.updateRoom(id, roomDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}

