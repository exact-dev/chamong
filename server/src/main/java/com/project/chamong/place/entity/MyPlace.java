package com.project.chamong.place.entity;

import com.project.chamong.audit.Auditable;
import com.project.chamong.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

// 내가 찾은 차박지
@Entity
@Getter
@Setter
public class MyPlace extends Auditable {
    // 장소 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 설명
    private String memo;
    
    // 내가 찾은 차박지 이미지
    String placeImg;
    
    // 키워드
    @ElementCollection
    @CollectionTable(name = "my_place_keyword")
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();
    
    // 위도
    private Double mapX;
    
    // 경도
    private Double mapY;
    
    // 공유 상태
    private Boolean isShared;

    // 멤버 고유 키
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    public void setMember(Member member){
        if(this.member != null){
            this.member.getMyPlaces().remove(this);
        }
        this.member = member;
        member.getMyPlaces().add(this);
    }

}
