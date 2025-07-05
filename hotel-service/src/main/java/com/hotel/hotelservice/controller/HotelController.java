package com.hotel.hotelservice.controller;

import com.hotel.hotelservice.dto.HotelDTO;
import com.hotel.hotelservice.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public HotelDTO create(@RequestBody @Valid HotelDTO hotelDTO) {
        return hotelService.createHotel(hotelDTO);
    }

    @GetMapping("/{id}")
    public HotelDTO getById(@PathVariable Long id) {
        return hotelService.getHotel(id);
    }

    @GetMapping
    public List<HotelDTO> getAll() {
        return hotelService.getAllHotels();
    }

    @PutMapping("/{id}")
    public HotelDTO update(@PathVariable Long id, @RequestBody @Valid HotelDTO hotelDTO) {
        return hotelService.updateHotel(id, hotelDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        hotelService.deleteHotel(id);
    }
}
