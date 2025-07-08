package com.hotel.hotelservice.service.impl;

import com.hotel.hotelservice.dto.HotelDTO;
import com.hotel.hotelservice.entity.Hotel;
import com.hotel.hotelservice.repository.HotelRepository;
import com.hotel.hotelservice.service.HotelService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Transactional
    @Override
    public HotelDTO createHotel(HotelDTO dto) {
        hotelRepository.findByName(dto.getName())
                .ifPresent(r -> {
                    throw new EntityExistsException("Hotel already exists: " + dto.getName());
                });
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(dto, hotel);
        return toDTO(hotelRepository.save(hotel));
    }

    @Override
    public HotelDTO getHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        return toDTO(hotel);
    }

    @Override
    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    @Override
    public HotelDTO updateHotel(Long id, HotelDTO dto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setStarRating(dto.getStarRating());

        return toDTO(hotelRepository.save(hotel));
    }
    @Transactional
    @Override
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    private HotelDTO toDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        BeanUtils.copyProperties(hotel, dto);
        return dto;
    }
}
