package com.project.chamong.article.entity;

import com.project.chamong.audit.BaseTime;
import com.project.chamong.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ArticleLike extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    
    public void setArticle(Article article) {
        if(this.article != null){
            this.article.getArticleLikes().remove(this);
        }
        this.article = article;
        article.getArticleLikes().add(this);
    }
    
    public void setMember(Member member) {
        if(this.member != null){
            this.member.getArticleLikes().remove(this);
        }
        this.member = member;
        member.getArticleLikes().add(this);
    }
}
