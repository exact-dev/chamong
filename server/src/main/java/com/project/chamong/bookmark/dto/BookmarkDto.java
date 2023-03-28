package com.project.chamong.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class BookmarkDto {

    @Getter
    public static class Post{
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response{
        private long bookmarkId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
