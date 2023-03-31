package com.project.chamong.place.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MyPlaceDto {
    @Getter
    @Setter
    public static class Post {
        private String memo;
        private List<String> keywords;
        private Double mapX;
        private Double mapY;
        private String myPlaceImg;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private Long id;
        private String memo;
        private List<String> keywords;
        private Double mapX;
        private Double mapY;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String placeImg;
        private Long memberId;
        private Boolean isShared;

    }

    @Getter
    @Setter
    public static class Patch {
        private String memo;
        private List<String> keywords;
        private Boolean isShared;
        private String myPlaceImg;
    }
}
