package com.project.chamong.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class BookmarkDto {

    @Getter
    @AllArgsConstructor
    public static class Post{

        @Positive
        private long memberId;

        @Positive
        private long contentId;
    }

    public static class Response{
        private long bookmarkId;
        private long memberId;
        private long contentId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedat;
    }
}
