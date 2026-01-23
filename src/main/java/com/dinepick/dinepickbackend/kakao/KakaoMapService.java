package com.dinepick.dinepickbackend.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoMapService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoPlaceResponse search(String keyword, int page) {

        String url = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/keyword.json")
                .queryParam("query", keyword)
                .queryParam("page", page)
                .queryParam("size", 15)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoPlaceResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        KakaoPlaceResponse.class
                );

        return response.getBody();
    }
}
