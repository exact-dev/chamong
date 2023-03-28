package com.project.chamong.camping.dto;

import com.project.chamong.camping.entity.Content;
import com.project.chamong.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponseDto {
    private List<Review> reviews;

    private double totalRating;

    private long contentId;

    // 업체명
    private String facltNm;

    // 한줄소개
    private String lineIntro;

    // 자세한 설명
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

    // 수정 날짜
    private String modifiedtime;

    // 유명 시설 기준 거리 및 관광지 한줄 평
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
}
