package com.project.chamong.article.dto;

import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArticleDto {
    private Long id;
    private String nickName;
    private String title;
    private String content;
    private String articleImg;
    private int viewCnt;
    private int likeCnt;
    private int commentCnt;
    private List<Comment> comments = new ArrayList<>();
    private List<ArticleLike> articleLikes = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Member member;
    private Long memberId;

    @Getter
    @Setter
    public static class Response {
        private Long id;
        @NotBlank(message = "제목을 입력해주세요")
        private String title;
        @NotBlank(message = "내용을 입력해주세요")
        private String content;
        private String nickName;
        private String articleImg;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean like;
        private Long memberId;
        private int viewCnt;
        private int likeCnt;
        private int commentCnt;

    }

    @Getter
    @Setter
    public static class Post {
        private String title;
        private String content;
        private String articleImg;
        private Long memberId;
        private String nickName;
    }

    @Getter
    @Setter
    public static class Patch {
        private String title;
        private String content;
        private String articleImg;
    }
}
