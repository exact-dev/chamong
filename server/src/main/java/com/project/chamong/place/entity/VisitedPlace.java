package com.project.chamong.place.entity;

import com.project.chamong.audit.BaseTime;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.member.entity.Member;
import com.project.chamong.place.dto.VisitedPlaceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class VisitedPlace extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Content content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    
    public void setMember(Member member){
        if(this.member != null){
            this.member.getVisitedPlaces().remove(this);
        }
        this.member = member;
        member.getVisitedPlaces().add(this);
    }
    
    @Builder
    public VisitedPlace(Content content) {
        this.content = content;
    }
    
    public static VisitedPlace createVisitedPlace(VisitedPlaceDto.Post postDto){
        return VisitedPlace.builder()
          .content(postDto.getContent())
          .build();
    }
}
