package com.hotel.hotelservice.service;

import com.hotel.hotelservice.dto.HotelDTO;
import java.util.List;

public interface HotelService {
    HotelDTO createHotel(HotelDTO hotelDTO);
    HotelDTO getHotel(Long id);
    List<HotelDTO> getAllHotels();
    HotelDTO updateHotel(Long id, HotelDTO hotelDTO);
    void deleteHotel(Long id);
}
