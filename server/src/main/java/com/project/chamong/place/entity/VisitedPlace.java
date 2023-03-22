package com.project.chamong.place.entity;

import com.project.chamong.audit.Auditable;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.member.entity.Member;
import com.project.chamong.place.dto.VisitedPlaceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class VisitedPlace extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    public void setMember(Member member){
        if(this.member != null){
            this.member.getVisitedPlaces().remove(this);
        }
        this.member = member;
        member.getVisitedPlaces().add(this);
    }
    @Builder
    public VisitedPlace(Content content, Member member) {
        this.content = content;
        this.member = member;
    }
}
