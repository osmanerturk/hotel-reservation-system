package com.hotel.hotelservice.service.impl;

import com.hotel.common.dto.RoomDTO;
import com.hotel.hotelservice.entity.Room;
import com.hotel.hotelservice.repository.RoomRepository;
import com.hotel.hotelservice.service.RoomService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    @Override
    public RoomDTO createRoom(RoomDTO dto) {
        roomRepository.findByRoomNumberAndHotelId(dto.getRoomNumber(), dto.getHotelId())
                .ifPresent(r -> {
                    throw new EntityExistsException("Room already exists for hotelId: " + dto.getHotelId() + " and roomNumber: " + dto.getRoomNumber());
                });
        Room room = new Room();
        BeanUtils.copyProperties(dto, room);
        return toDTO(roomRepository.save(room));
    }

    @Override
    public RoomDTO getRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return toDTO(room);
    }

    @Override
    public RoomDTO getRoomWithHotelIdAndRoomNumber(String roomNumber, Long hotelId) {
        Room room = roomRepository.findByRoomNumberAndHotelId(roomNumber, hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found for hotelId: " + hotelId + " and roomNumber: " + roomNumber));
        return toDTO(room);
    }

    @Override
    public List<RoomDTO> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RoomDTO updateRoom(Long id, RoomDTO dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoomNumber(dto.getRoomNumber());
        room.setCapacity(dto.getCapacity());
        room.setPricePerNight(dto.getPricePerNight());

        return toDTO(roomRepository.save(room));
    }

    @Transactional
    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    private RoomDTO toDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        BeanUtils.copyProperties(room, dto);
        return dto;
    }
}
