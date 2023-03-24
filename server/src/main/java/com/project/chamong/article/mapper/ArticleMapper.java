package com.project.chamong.article.mapper;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    
    ArticleDto.Response toArticleResponse(Article article);
    
    default ArticleDto.Response toArticleResponse(Article article, Member member){
        
        Boolean isLiked = member.getArticleLikes()
          .stream()
          .anyMatch(memberArticleLike -> article.getArticleLikes().stream()
            .anyMatch(articleArticleLike -> memberArticleLike.getId() == articleArticleLike.getId()));
        
        return ArticleDto.Response.builder()
          .id(article.getId())
          .title(article.getTitle())
          .content(article.getContent())
          .nickname(article.getMember().getNickname())
          .profileImg(article.getMember().getProfileImg())
          .carName(article.getMember().getCarName())
          .articleImg(article.getArticleImg())
          .memberId(member.getId())
          .viewCnt(article.getViewCnt())
          .likeCnt(article.getLikeCnt())
          .commentCnt(article.getCommentCnt())
          .createdAt(article.getCreatedAt())
          .updatedAt(article.getUpdatedAt())
          .isLiked(isLiked)
          .build();
    };
}
