package com.project.chamong.article.dto;

import com.project.chamong.article.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    @Getter
    @Setter
    public static class Post {
        private String content;
        private String nickname;
        private String profileImg;
        private Long articleId;
    }
    @Getter
    @Setter
    @Builder
    public static class Response {
        private Long id;
        private String content;
        private Long articleId;
        private Long memberId;
        private String nickname;
        private String profileImg;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Getter
    @Setter
    public static class Patch{
        private String content;
        
    }

}
