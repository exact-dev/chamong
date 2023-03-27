package com.project.chamong.review.entity;

import com.project.chamong.audit.BaseTime;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Review extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content contents;
}
