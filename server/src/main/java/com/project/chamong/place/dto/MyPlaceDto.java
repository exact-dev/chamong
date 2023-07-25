package com.project.chamong.place.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MyPlaceDto {
    @Getter
    @Setter
    @Builder
    public static class Post {
        @NotBlank(message = "메모는 공백일 수 없습니다.")
        private String memo;
        
        private List<String> keywords;
        
        @NotNull(message = "좌표X 는 공백일 수 없습니다.")
        private Double mapX;
        
        @NotNull(message = "좌표Y 는 공백일 수 없습니다.")
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
    @Builder
    public static class Patch {
        @NotBlank(message = "메모는 공백일 수 없습니다.")
        private String memo;
        
        private List<String> keywords;
        
        private Boolean isShared;
        
        private String myPlaceImg;
    }
}
