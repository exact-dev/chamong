package com.project.chamong.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLikeDto {
    private Long id;
    private Long articleId;
    private Long memberId;

    public ArticleLikeDto(Long articleId, Long memberId){
        this.articleId = articleId;
        this.memberId = memberId;
    }
}
