package com.hotel.hotelservice.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.common.dto.RoomDTO;
import com.hotel.hotelservice.entity.Room;
import com.hotel.hotelservice.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();
        jdbcTemplate.execute("ALTER SEQUENCE room_id_seq RESTART WITH 1");
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    void createRoom_shouldReturnCreatedRoom() throws Exception {
        RoomDTO dto = new RoomDTO();
        dto.setHotelId(1L);
        dto.setRoomNumber("102");
        dto.setCapacity(2);
        dto.setPricePerNight(new BigDecimal("200.00"));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("102"))
                .andExpect(jsonPath("$.capacity").value(2));
    }

    @Test
    void getRoom_shouldReturnRoom() throws Exception {
        Room room = Room.builder()
                .hotelId(1L)
                .roomNumber("103")
                .capacity(3)
                .pricePerNight(new BigDecimal("300.00"))
                .build();
        room = roomRepository.save(room);

        mockMvc.perform(get("/api/rooms/" + room.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("103"))
                .andExpect(jsonPath("$.capacity").value(3));
    }

    @Test
    void getRoomsByHotelId_shouldReturnList() throws Exception {
        Room room1 = Room.builder().hotelId(1L).roomNumber("201").capacity(2).pricePerNight(new BigDecimal("250.00")).build();
        Room room2 = Room.builder().hotelId(1L).roomNumber("202").capacity(3).pricePerNight(new BigDecimal("300.00")).build();
        roomRepository.save(room1);
        roomRepository.save(room2);

        mockMvc.perform(get("/api/rooms/hotel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getRoomWithRoomNumberAndHotelId_shouldReturnRoom() throws Exception {
        Room room = Room.builder()
                .hotelId(2L)
                .roomNumber("301")
                .capacity(1)
                .pricePerNight(new BigDecimal("150.00"))
                .build();
        roomRepository.save(room);

        mockMvc.perform(get("/api/rooms/301/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("301"))
                .andExpect(jsonPath("$.hotelId").value(2));
    }
}
