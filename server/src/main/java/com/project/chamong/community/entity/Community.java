package com.project.chamong.community.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityId; // PK

    // private Long memberId;
    private String writer; // 작성자
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private String articleImg;
    private int view;
    private int likeCount;
    private int commentCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updateAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setCommunity(this);
    }
    public void removeComment(Comment comment){
        this.comments.remove(comment);
        comment.setCommunity(null);
    }

    public void update(Community community){
        this.title = community.getTitle();
        this.content = community.getContent();
        this.updateAt = LocalDateTime.now();
    }
    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }
    public void increaseCommentCount(){
        this.commentCount++;
    }

    public void decreaseCommentCount(){
        this.commentCount--;
    }

    public Community toEntity(){
        return Community.builder()
                .title(title)
                .content(content)
                .articleImg(articleImg)
                .writer(writer)
                .build();
    }
}
