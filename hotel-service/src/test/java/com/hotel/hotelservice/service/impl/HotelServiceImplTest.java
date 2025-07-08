package com.hotel.hotelservice.service.impl;

import com.hotel.hotelservice.dto.HotelDTO;
import com.hotel.hotelservice.entity.Hotel;
import com.hotel.hotelservice.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;


    @Test
    void testCreateHotel() {
        HotelDTO dto = new HotelDTO();
        dto.setName("Test Hotel");
        dto.setAddress("Test Address");
        dto.setStarRating(5);

        Hotel savedHotel = Hotel.builder()
                .id(1L)
                .name(dto.getName())
                .address(dto.getAddress())
                .starRating(dto.getStarRating())
                .build();

        when(hotelRepository.save(any(Hotel.class))).thenReturn(savedHotel);

        HotelDTO result = hotelService.createHotel(dto);

        assertNotNull(result);
        assertEquals("Test Hotel", result.getName());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testGetHotelFound() {
        Hotel hotel = Hotel.builder().id(1L).name("Hotel 1").address("Address").starRating(3).build();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        HotelDTO dto = hotelService.getHotel(1L);

        assertEquals("Hotel 1", dto.getName());
    }

    @Test
    void testGetHotelNotFound() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hotelService.getHotel(999L));
    }

    @Test
    void testDeleteHotel() {
        doNothing().when(hotelRepository).deleteById(1L);
        hotelService.deleteHotel(1L);
        verify(hotelRepository, times(1)).deleteById(1L);
    }
}
