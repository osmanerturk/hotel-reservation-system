package com.hotel.hotelservice.service.impl;

import com.hotel.common.dto.RoomDTO;
import com.hotel.hotelservice.entity.Room;
import com.hotel.hotelservice.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;


    @Test
    void createRoom_shouldSaveAndReturnRoom() {
        RoomDTO dto = new RoomDTO();
        dto.setHotelId(1L);
        dto.setRoomNumber("101");
        dto.setCapacity(2);
        dto.setPricePerNight(BigDecimal.valueOf(150));

        Room savedRoom = Room.builder()
                .id(1L)
                .hotelId(dto.getHotelId())
                .roomNumber(dto.getRoomNumber())
                .capacity(dto.getCapacity())
                .pricePerNight(dto.getPricePerNight())
                .build();

        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);

        RoomDTO result = roomService.createRoom(dto);

        assertNotNull(result);
        assertEquals("101", result.getRoomNumber());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void getRoom_shouldReturnRoomDTO() {
        Room room = Room.builder().id(1L).hotelId(1L).roomNumber("101").capacity(2).pricePerNight(BigDecimal.valueOf(150)).build();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RoomDTO dto = roomService.getRoom(1L);

        assertEquals("101", dto.getRoomNumber());
    }

    @Test
    void getRoom_shouldThrowWhenNotFound() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roomService.getRoom(999L));
    }

    @Test
    void getRoomsByHotelId_shouldReturnRoomList() {
        List<Room> rooms = List.of(
                Room.builder().id(1L).hotelId(1L).roomNumber("101").capacity(2).pricePerNight(BigDecimal.valueOf(150)).build()
        );
        when(roomRepository.findByHotelId(1L)).thenReturn(rooms);

        List<RoomDTO> result = roomService.getRoomsByHotelId(1L);
        assertEquals(1, result.size());
        assertEquals("101", result.get(0).getRoomNumber());
    }

    @Test
    void deleteRoom_shouldCallRepository() {
        doNothing().when(roomRepository).deleteById(1L);
        roomService.deleteRoom(1L);
        verify(roomRepository, times(1)).deleteById(1L);
    }
}