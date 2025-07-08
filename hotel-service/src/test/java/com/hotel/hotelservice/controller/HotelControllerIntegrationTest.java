package com.hotel.hotelservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.hotelservice.dto.HotelDTO;
import com.hotel.hotelservice.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HotelRepository hotelRepository;

    private HotelDTO testHotel;

    @BeforeEach
    void setup() {
        hotelRepository.deleteAll();
        testHotel = new HotelDTO();
        testHotel.setName("Integration Hotel");
        testHotel.setAddress("Integration Address");
        testHotel.setStarRating(5);
    }

    @Test
    void createHotel_shouldReturnCreatedHotel() throws Exception {
        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testHotel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Hotel"));
    }

    @Test
    void getAllHotels_shouldReturnList() throws Exception {
        mockMvc.perform(post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHotel)));

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateHotel_shouldUpdateSuccessfully() throws Exception {
        String content = mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testHotel)))
                .andReturn().getResponse().getContentAsString();

        HotelDTO created = objectMapper.readValue(content, HotelDTO.class);
        created.setName("Updated Name");

        mockMvc.perform(put("/api/hotels/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void deleteHotel_shouldReturn200() throws Exception {
        String content = mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testHotel)))
                .andReturn().getResponse().getContentAsString();

        HotelDTO created = objectMapper.readValue(content, HotelDTO.class);

        mockMvc.perform(delete("/api/hotels/" + created.getId()))
                .andExpect(status().isOk());

        assertThat(hotelRepository.findById(created.getId())).isEmpty();
    }
}
