package com.project.chamong.camping.entity;

import com.project.chamong.camping.dto.CampingApiDto;
import com.project.chamong.review.entity.Review;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;
//    private Long id;
    
    @OneToMany(mappedBy = "contents")
    private List<Review> reviews = new ArrayList<>();

    // 업체명
    private String facltNm;

    // 한줄소개
    @Column(columnDefinition = "TEXT")
    private String lineIntro;

    // 자세한 설명
    @Column(columnDefinition = "TEXT")
    private String intro;

    // 테마 추천 키워드
    private String themaEnvrnCl;

    // 위도
    private double mapX;

    // 경도
    private double mapY;

    // 상세 주소
    private String addr1;

    // 전화번호
    private String tel;

    // 홈페이지 주소
    private String homepage;

    // 예약 방법
    private String resveCl;

    // 지역
    private String doNm;

    // 운영 여부(enum)
    private String manageSttus;

    // 야영장, 글램핑 종류
    private String induty;

    // 대표 이미지 주소
    private String firstImageUrl;

    // 등록 날짜
    private String createdtime;
//    private String createdTime;

    // 수정 날짜
    private String modifiedtime;
//    private String modifiedTime;

    // 유명 시설 기준 거리 및 관광지 한줄 평
    @Column(columnDefinition = "TEXT")
    private String featureNm;

    // 화로대 개별 및 공용 여부
    private String brazierCl;

    // 글램핑 구비 용품
    private String glampInnerFclty;

    // 카라반 구비 용품
    private String caravInnerFclty;

    // 공통 기능 및 구비용품
    private String sbrsCl;

    // 반려동물 여부
    private String animalCmgCl;

    // 체험시설 여부
    private String exprnProgrmAt;

    // 체험시설 종류
    private String exprnProgrm;

    // 부수적 체험시설 및 테마
    private String posblFcltyCl;

    // 섬, 호수, 강 등등 종류
    private String lctCl;
    @Builder
    public Content(CampingApiDto.Post campingApiPostDto) {
        facltNm = campingApiPostDto.getFacltNm();
        lineIntro = campingApiPostDto.getLineIntro();
        intro = campingApiPostDto.getIntro();
        themaEnvrnCl = campingApiPostDto.getThemaEnvrnCl();
        mapX = campingApiPostDto.getMapX();
        mapY = campingApiPostDto.getMapY();
        addr1 = campingApiPostDto.getAddr1();
        tel = campingApiPostDto.getTel();
        homepage = campingApiPostDto.getHomepage();
        resveCl = campingApiPostDto.getResveCl();
        doNm = campingApiPostDto.getDoNm();
        manageSttus = campingApiPostDto.getManageSttus();
        induty = campingApiPostDto.getInduty();
        firstImageUrl = campingApiPostDto.getFirstImageUrl();
        createdtime = campingApiPostDto.getCreatedtime();
        modifiedtime = campingApiPostDto.getModifiedtime();
        featureNm = campingApiPostDto.getFeatureNm();
        brazierCl = campingApiPostDto.getBrazierCl();
        glampInnerFclty = campingApiPostDto.getGlampInnerFclty();
        caravInnerFclty = campingApiPostDto.getCaravInnerFclty();
        sbrsCl = campingApiPostDto.getSbrsCl();
        animalCmgCl = campingApiPostDto.getAnimalCmgCl();
        exprnProgrmAt = campingApiPostDto.getExprnProgrmAt();
        exprnProgrm = campingApiPostDto.getExprnProgrm();
        posblFcltyCl = campingApiPostDto.getPosblFcltyCl();
        lctCl = campingApiPostDto.getLctCl();
    }

}
