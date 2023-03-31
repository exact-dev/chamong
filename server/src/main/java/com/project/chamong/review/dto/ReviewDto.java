package com.project.chamong.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class ReviewDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post{
        private int rating;

        @NotBlank
        private String content;

    }

    @Getter
    @Setter
    public static class Patch{
        private int rating;

        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response{
        private long reviewId;
        private int rating;
        private String content;

    }
}
