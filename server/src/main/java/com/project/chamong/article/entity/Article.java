package com.project.chamong.article.entity;

import com.project.chamong.member.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK
    // 작성자 닉네임
    private String nickName;
    // 제목
    @NotEmpty
    private String title;
    // 내용
    @NotEmpty
    private String content;
    // 업로드 하는 이미지
    private String articleImg;
    // 조회수
    private int viewCnt;
    // 좋아요 수
    private int likeCnt;
    // 댓글 수
    private int commentCnt;
    // 댓글
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    // 좋아요
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleLike> articleLikes = new ArrayList<>();
    // 생성 시간
    @CreatedDate
    private LocalDateTime createdAt;
    // 업데이트 시간
    @LastModifiedDate
    private LocalDateTime updatedAt;
    // 작성자 확인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long memberId;

    public Article(String title, String content, Member nickname) {
        this.title = title;
        this.content = content;
        this.nickName = String.valueOf(nickname);
    }


//    public void addComment(Comment comment) {
//        this.comments.add(comment);
//        comment.setArticle(this);
//    }

//    public void removeComment(Comment comment) {
//        this.comments.remove(comment);
//        comment.setArticle(null);
//    }
//
//    public void update(Article community) {
//        this.title = community.getTitle();
//        this.content = community.getContent();
//        this.updatedAt = LocalDateTime.now();
//    }
//
    public void increaseLikeCnt() {
        this.likeCnt++;
    }

    public void decreaseLikeCnt() {
        this.likeCnt--;
    }

    public void increaseCommentCnt() {
        this.commentCnt++;
    }

    public void decreaseCommentCnt() {
        this.commentCnt--;
    }

    // null 체크를 하지 않으면 @Setter를 사용하므로써 문제가 생길 수 있음
    public void update(Article updatedArticle) {
        if (updatedArticle == null) {
            throw new IllegalArgumentException("updated article cannot be null");
        }
        if (updatedArticle.getTitle() != null) {
            this.setTitle(updatedArticle.getTitle());
        }
        if (updatedArticle.getContent() != null) {
            this.setContent(updatedArticle.getContent());
        }
        if (updatedArticle.getArticleImg() != null) {
            this.setArticleImg(updatedArticle.getArticleImg());
        }
        if (updatedArticle.getMemberId() != null) {
            this.setMemberId(updatedArticle.getMemberId());
        }
    }

}
