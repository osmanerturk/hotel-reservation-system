package com.hotel.reservationservice.client;



import com.hotel.common.dto.RoomDTO;
import com.hotel.common.security.CurrentRequestTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class HotelServiceClient {

    private final WebClient webClient;

    public HotelServiceClient(WebClient.Builder builder,
                              @Value("${services.hotel}") String hotelServiceUrl) {
        this.webClient = builder.baseUrl(hotelServiceUrl).build();
    }

    public boolean hotelExists(String roomNumber, Long hotelId) {
        try {
            String token = CurrentRequestTokenHolder.getToken();

            RoomDTO room = webClient.get()
                    .uri("/api/rooms/{roomNumber}/{hotelId}", roomNumber, hotelId)
                    .headers(headers -> headers.setBearerAuth(token))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            res -> Mono.error(new RuntimeException("Room service returned error")))
                    .bodyToMono(RoomDTO.class)
                    .block();

            return room != null;
        } catch (Exception e) {
            log.warn("Hotel ID {} not found or room service error: {}", hotelId, e.getMessage());
            return false;
        }
    }
}
