package com.project.chamong.place.dto;

import com.project.chamong.camping.entity.Content;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class VisitedPlaceDto {
    @Builder
    @Getter
    public static class Post{
        private Content content;
    }
    @Getter
    @Setter
    @Builder
    public static class Response{
        // 장소 ID
        private Long id;
        private Long memberId;
        private Long contentId;
        private String facltNm;
        private String lineIntro;
        private String addr1;
        private String firstImageUrl;
        private double mapX;
        private double mapY;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
