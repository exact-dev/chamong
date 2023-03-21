package com.project.chamong.camping.service;

import com.project.chamong.camping.dto.CampingApiDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.repository.CampingApiRepository;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.review.entity.Review;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampingApiService {
    private CampingApiRepository campingApiRepository;
    Page<Content> content;
    private final int pageSize = 30;

    public CampingApiService(CampingApiRepository campingApiRepository) {
        this.campingApiRepository = campingApiRepository;
    }

    // 특정 캠핑장 찾기
    public Content findContent(long contentId){
        return findVerifiedContent(contentId);
    }

    // 캠핑장 전체 리스트
    public Page<Content> findContents(int page) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        content = campingApiRepository.findContents(pageRequest);
        return content;
    }

    public Content findVerifiedContent(long contentId){
        Optional<Content> optionalContent =
                campingApiRepository.findById(contentId);
        Content findContent =
                optionalContent.orElseThrow(() ->
                         new BusinessLogicException(ExceptionCode.CONTENT_NOT_FOUND));
        return findContent;
    }

    // 고캠핑 API
    public Content postCampingApi(Content content) {
        return campingApiRepository.save(content);
    }

    // 고캠핑 API 특정 키워드 검색
    public Page<Content> findKeyword(int page,
                                     int keywordId) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        switch (keywordId) {
            // 오션뷰
            case 1:
                content = campingApiRepository.findByLctClContaining("해변", pageRequest);
                break;
            // 피톤치드
            case 2:
                content = campingApiRepository.findByLctClContaining("숲", pageRequest);
                break;
            // 애견동반
            case 3:
                content = campingApiRepository.findByAnimalCmgClNotContaining("불가능", pageRequest);
                break;
            // 운동
            case 4:
                content = campingApiRepository.findBySbrsClContaining("운동", pageRequest);
                break;
            // 물놀이 시간
            case 5:
                content = campingApiRepository.findByLctClContaining("물놀이", pageRequest);
                break;
            // 단풍
            case 6:
                content = campingApiRepository.findByThemaEnvrnClContaining("단풍", pageRequest);
                break;
            // 봄꽃여행
            case 7:
                content = campingApiRepository.findByThemaEnvrnClContaining("봄꽃", pageRequest);
                break;
            // 일몰명소
            case 8:
                content = campingApiRepository.findByThemaEnvrnClContaining("일몰", pageRequest);
                break;
        }

        return content;
    }

    public Page<Content> findCamping(int page,
                                     String keyword,
                                     int themaId,
                                     int placeId) {

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("createdtime").descending());
        String place = null;
        //대구/경북, 경기/인천, 대구/충청 등등 두 개의 지역이 합쳐서 들어올 때
        String placeSecond = null;
        String thema = null;

        // 지역
        switch (placeId) {
            // 서울
            case 1:
                place = "서울";
                break;
            // 대구/경북
            case 2:
                place = "대구";
                placeSecond = "경상북도";
                break;
            // 강원
            case 3:
                place = "강원";
                break;
            // 경기/인천
            case 4:
                place = "경기";
                placeSecond = "인천";
                break;
            // 광주/전라
            case 5:
                place = "광주";
                placeSecond = "전라";
                break;
            // 대전/충청
            case 6:
                place = "대전";
                placeSecond = "충청";
                break;
            // 제주
            case 7:
                place = "제주";
                break;
            // 부산/경남
            case 8:
                place = "부산";
                placeSecond = "경상남도";
                break;
            default:
                place = "none";
                placeSecond = "none";
                break;
        }

        // 테마 및 편의사항
        switch (themaId) {
            // 화장실
            case 1:
                thema = "화장실";
                break;
            // 산
            case 2:
                thema = "산";
                break;
            // 강
            case 3:
                thema = "강";
                break;
            // 섬
            case 4:
                thema = "섬";
                break;
            // 숲
            case 5:
                thema = "숲";
                break;
            // 호수
            case 6:
                thema = "호수";
                break;
            // 해변
            case 7:
                thema = "해변";
                break;
            // 와이파이
            case 8:
                thema = "무선인터넷";
                break;
            // 전기
            case 9:
                thema = "전기";
                break;
            // 운동시설
            case 10:
                thema = "운동시설";
                break;
            // 물놀이
            case 11:
                thema = "물놀이";
                break;
            // 마트
            case 12:
                thema = "마트";
                break;
            // 편의점
            case 13:
                thema = "편의점";
                break;
            // 체험활동
            case 14:
                thema = "Y";
                break;
            // 낚시
            case 15:
                thema = "낚시";
                break;
            // 반려동물
            case 16:
                thema = "가능";
                break;
            // 운영중
            case 17:
                thema = "운영";
                break;
            default:
                thema = "none";
                break;
        }
        if (keyword == null || keyword.isEmpty())
            keyword = "none";
        content = campingApiRepository.findCamping(keyword, place, placeSecond, thema, pageRequest);
        return content;
    }
}