package com.project.chamong.article.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleLikeDto {
    @Getter
    @Setter
    public static class Post{
        private Long articleId;
        private Long memberId;
    }
}
