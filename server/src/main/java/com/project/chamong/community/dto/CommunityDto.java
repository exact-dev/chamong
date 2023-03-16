package com.project.chamong.community.dto;

import com.project.chamong.community.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommunityDto {
    private Long communityId;
    private String title;
    private String content;
    private String articleImg;

    //private UserDto nickname;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean like;

    public CommunityDto(final Article entity){
        this.communityId = entity.getCommunityId();
        this.content = entity.getContent();
        this.title = entity.getTitle();
        this.articleImg = entity.getArticleImg();
    }

    public Article toEntity(){
        return Article.builder()
                .communityId(communityId)
                .title(title)
                .content(content)
                .articleImg(articleImg)
                .build();
    }

}
