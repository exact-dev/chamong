package com.project.chamong.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@AllArgsConstructor
public class ReviewDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post{

        private int rating;

        @NotBlank
        private String content;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private long reviewId;
        private long memberId;
        private long contentId;
        private int rating;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedat;

    }
}
