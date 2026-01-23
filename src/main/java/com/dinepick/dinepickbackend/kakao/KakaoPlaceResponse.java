package com.dinepick.dinepickbackend.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KakaoPlaceResponse {

    private Meta meta;
    private List<Document> documents;

    @Getter
    public static class Meta {

        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("total_count")
        private int totalCount;
    }

    @Getter
    public static class Document {
        private String id;
        private String place_name;
        private String address_name;
        private String x; // longitude
        private String y; // latitude
        private String category_name;
    }
}
