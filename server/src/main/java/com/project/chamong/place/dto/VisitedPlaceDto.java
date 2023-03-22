package com.project.chamong.place.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class VisitedPlaceDto {
    @Getter
    @Setter
    public static class Response{
        // 장소 ID
        private Long id;
        private Long memberId;
        private String facltNm;
        private String lineIntro;
        private String addr1;
        private String firstImageUrl;
        private double mapX;
        private double mapY;
        private String createdAt;
        private String updatedAt;
    }
}
