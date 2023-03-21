package com.project.chamong.place.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class VisitedPlaceDto {
    @Getter
    @Setter
    public static class Post{
        // 장소명
        private String placeName;
        // 주소
        private String placeAddress;
        // 장소 설명
        private String placeDescription;
        // 위도
        private Double latitude;
        // 경도
        private Double longitude;
        // 방문 날짜
        private LocalDateTime placedAt;
        // 메모
        private String memo;
        private Long memberId;
    }
    @Getter
    @Setter
    public static class Response{
        // 장소 ID
        private Long placeId;
        // 장소명
        private String placeName;
        // 주소
        private String placeAddress;
        // 장소 설명
        private String placeDescription;
        // 위도
        private Double latitude;
        // 경도
        private Double longitude;
        // 방문 날짜
        private LocalDateTime placedAt;
        // 수정 날짜
        private LocalDateTime updatedAt;
        // 메모
        private String memo;
        private Long memberId;
    }
    @Getter
    @Setter
    public static class Patch{
        // 장소명
        private String placeName;
        // 주소
        private String placeAddress;
        // 장소 설명
        private String placeDescription;
        // 위도
        private Double latitude;
        // 경도
        private Double longitude;
        // 방문 날짜
        private LocalDateTime placedAt;
        // 수정 날짜
        private LocalDateTime updatedAt;
        // 메모
        private String memo;
    }
}
