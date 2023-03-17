package com.project.chamong.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ReviewDto {

    @Getter
    @AllArgsConstructor
    public static class Post{

        @Positive
        private long memberId;

        @Positive
        private long contentId;

        private int rating;

        @NotBlank
        private String content;

    }


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
