package com.project.chamong.place.entity;

import com.project.chamong.member.entity.Member;
import com.project.chamong.place.dto.MyPlaceDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

// 내가 찾은 차박지
@Entity
@Getter
@Setter
public class MyPlace {
    // 장소 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 설명
    // 필수값
    @NotEmpty
    private String memo;
    // 키워드
    private String keyword;
    // 내가 찾은 차박지에 관련 이미지
    private String image;
    // 위도
    // 필수값
    @NotNull
    private Double latitude;
    // 경도
    // 필수값
    @NotNull
    private Double longitude;
    // 생성 날짜
    @CreatedDate
    private LocalDateTime placedAt;
    // 공유 상태
    private boolean shared;

    // 멤버 고유 키
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static MyPlace createMyPlace(MyPlaceDto.Post postDto) {
        MyPlace myPlace = new MyPlace();
        myPlace.setMemo(myPlace.getMemo());
        myPlace.setKeyword(myPlace.getKeyword());
        myPlace.setImage(myPlace.getImage());
        myPlace.setLatitude(myPlace.getLatitude());
        myPlace.setLongitude(myPlace.getLongitude());
        return myPlace;
    }

    public void update(MyPlaceDto.Patch patchDto) {
        if (patchDto.getMemo() != null) {
            this.setMemo(patchDto.getMemo());
        }
        if (patchDto.getKeyword() != null) {
            this.setKeyword(patchDto.getKeyword());
        }
        if (patchDto.getLatitude() != null) {
            this.setLatitude(patchDto.getLatitude());
        }
        if (patchDto.getLongitude() != null) {
            this.setLongitude(patchDto.getLongitude());
        }
    }

}
