package com.project.chamong.place.entity;

import com.project.chamong.member.entity.Member;
import com.project.chamong.place.dto.VisitedPlaceDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class VisitedPlace {
    // 장소 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    // 장소명
    private String placeName;
    // 주소
    private String placeAddress;
    // 장소 설명
    private String placeDescription;
    // 위도
    private Double latitude;
    // 경도
    private Double longitude;
    // 방문 날짜
    private LocalDateTime placedAt;
    // 수정 날짜
    private LocalDateTime updatedAt;
    // 메모
    private String memo;
    //private Long memberId;

    // 멤버 고유 키
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static VisitedPlace createVisitedPlace(VisitedPlaceDto.Post postDto, Member member){
        VisitedPlace visitedPlace = new VisitedPlace();
        visitedPlace.setPlaceName(postDto.getPlaceName());
        visitedPlace.setPlaceAddress(postDto.getPlaceAddress());
        visitedPlace.setPlaceDescription(postDto.getPlaceDescription());
        visitedPlace.setLatitude(postDto.getLatitude());
        visitedPlace.setLongitude(postDto.getLongitude());
        visitedPlace.setPlacedAt(postDto.getPlacedAt());
        visitedPlace.setMember(member);
        visitedPlace.setMemo(postDto.getMemo());
        return visitedPlace;
    }


    public void update(VisitedPlaceDto.Patch patchDto) {
        if (patchDto.getPlaceName() != null) {
            this.setPlaceName(patchDto.getPlaceName());
        }
        if (patchDto.getPlaceAddress() != null) {
            this.setPlaceAddress(patchDto.getPlaceAddress());
        }
        if (patchDto.getPlaceDescription() != null) {
            this.setPlaceDescription(patchDto.getPlaceDescription());
        }
        if (patchDto.getLatitude() != null) {
            this.setLatitude(patchDto.getLatitude());
        }
        if (patchDto.getLongitude() != null) {
            this.setLongitude(patchDto.getLongitude());
        }
        if (patchDto.getPlacedAt() != null) {
            this.setPlacedAt(patchDto.getPlacedAt());
        }
        if (patchDto.getMemo() != null) {
            this.setMemo(patchDto.getMemo());
        }
    }
}
