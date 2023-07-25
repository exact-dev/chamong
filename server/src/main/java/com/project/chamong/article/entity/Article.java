package com.project.chamong.article.entity;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.audit.BaseTime;
import com.project.chamong.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK
    // 제목
    private String title;
    // 내용
    private String content;
    // 업로드 하는 이미지
    @Column(name = "image_url")
    private String articleImg;
    // 조회수
    private Integer viewCnt;
    // 좋아요 수
    private Integer likeCnt;
    
    // 댓글 수
    private Integer commentCnt;
    
    // 댓글
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();
    // 좋아요
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ArticleLike> articleLikes = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    
    public void increaseLikeCnt() {
        this.likeCnt++;
    }
    
    public void decreaseLikeCnt() {
        this.likeCnt--;
    }
    
    public boolean isWriter(Member member){
        return this.member.getId().equals(member.getId());
    }
    
    public void update(ArticleDto.Patch patchDto) {
        if (patchDto.getTitle() != null) {
            this.setTitle(patchDto.getTitle());
        }
        if (patchDto.getContent() != null) {
            this.setContent(patchDto.getContent());
        }
        if (patchDto.getArticleImg() != null) {
            this.setArticleImg(patchDto.getArticleImg());
        }
    }
    
    
    public static Article createArticle(ArticleDto.Post postDto, Member member) {
        Article article = new Article();
        article.setMember(member);
        article.title = postDto.getTitle();
        article.content = postDto.getContent();
        article.viewCnt = 0;
        article.commentCnt = 0;
        article.likeCnt = 0;
        article.articleImg = postDto.getArticleImg();
        return article;
    }
}