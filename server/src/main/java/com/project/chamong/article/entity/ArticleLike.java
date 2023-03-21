package com.project.chamong.article.entity;

import com.project.chamong.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ArticleLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "article_id")
//    private Long articleId;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "article_id")
//    private Article article;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

}
