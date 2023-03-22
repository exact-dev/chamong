package com.project.chamong.place.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyPlaceDto {
    @Getter
    @Setter
    public static class Post {
        private String memo;
        private String keyword;
        private String placeImg;
        private Double latitude;
        private Double longitude;
        private Boolean isShared;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private String memo;
        private String keyword;
        private Double latitude;
        private Double longitude;
        private String createdAt;
        private String updatedAt;
        private String placeImg;
        private Long memberId;
        private Boolean isShared;

    }

    @Getter
    @Setter
    public static class Patch {
        private String memo;
        private String keyword;
        private String placeImg;
        private Boolean isShared;
    }
}
