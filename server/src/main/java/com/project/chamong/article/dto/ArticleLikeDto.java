package com.project.chamong.article.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleLikeDto {
    private Long id;
    private Long articleId;
    private Long memberId;

    @Getter
    @Setter
    public static class Post{
        private Long articleId;
        private Long memberId;
    }
}
