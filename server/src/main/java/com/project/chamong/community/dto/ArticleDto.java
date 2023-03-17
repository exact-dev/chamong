package com.project.chamong.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String articleImg;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean like;
    public static class Post {
        private Long id;
        private String title;
        private String content;
        private String articleImg;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
        private boolean like;
    }


    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String articleImg;
        private LocalDateTime createAt;
        private LocalDateTime updateAd;
        private int like;
    }

}
