package com.project.chamong.camping.service;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.bookmark.repository.BookmarkRepository;
import com.project.chamong.camping.dto.ContentResponseDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.repository.CampingApiRepository;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.review.dto.ReviewDto;
import com.project.chamong.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CampingApiService {
    private final CampingApiRepository campingApiRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberService memberService;
    private final int pageSize = 30;

    // 특정 캠핑장 찾기
    public Content findContent(long contentId) {
        return campingApiRepository.findById(contentId).orElseThrow(() ->
                  new BusinessLogicException(ExceptionCode.CONTENT_NOT_FOUND));
    }

    // 특정 캠핑장 찾기(Response)
    public ContentResponseDto findContentResponse(long contentId, AuthorizedMemberDto authorizedMemberDto) {
        Member findMember = null;
        if (authorizedMemberDto != null) {
            findMember = memberService.findByEmail(authorizedMemberDto.getEmail());
        }
        
        Content findContent = campingApiRepository.findById(contentId)
          .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CONTENT_NOT_FOUND));
        
        return convertToContentResponse(findContent, findMember);
    }

    // 캠핑장 전체 리스트
    public List<ContentResponseDto> findContents(Long lastContentId, AuthorizedMemberDto authorizedMemberDto) {
        Member findMember;
        
        if (authorizedMemberDto != null) {
            findMember = memberService.findByEmail(authorizedMemberDto.getEmail());
        } else {
            findMember = null;
        }
        
        List<Content> contents = campingApiRepository.findContents(lastContentId, PageRequest.of(0, pageSize));
        
        return contents.stream().map(content -> convertToContentResponse(content, findMember)).collect(Collectors.toList());
    }


    // 위시리스트 조회
    public List<ContentResponseDto> findBookmark(Long lastBookmarkId, AuthorizedMemberDto authorizedMemberDto) {
        Member findMember;
        if (authorizedMemberDto != null) {
            findMember = memberService.findById(authorizedMemberDto.getId());
        } else {
            findMember = null;
        }
        long memberId = (findMember != null) ? findMember.getId() : 0L;
        
        List<Content> contents = campingApiRepository.findBookmark(lastBookmarkId, memberId, PageRequest.of(0, pageSize));
        
        return contents.stream().map(content -> convertToContentResponse(content, findMember)).collect(Collectors.toList());
    }


    public boolean isContentBookmarked(long memberId, long contentId) {
        boolean bookmarkYn = false;
        Integer response = bookmarkRepository.existsMemberIdAndContentContentIdAsInteger(memberId, contentId);
        if (response > 0) {
            bookmarkYn = true;
        }
        return bookmarkYn;
    }

    private MemberDto.Response convertToMemberDtoResponse(Member member) {
        return new MemberDto.Response(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getProfileImg(),
                member.getAbout(),
                member.getCarName(),
                member.getOilInfo()
        );
    }


    private ContentResponseDto convertToContentResponse(Content content, Member member) {
        ContentResponseDto dto = new ContentResponseDto();
        
        List<Review> reviews = content.getReviews();
        
        List<ReviewDto.Response> reviewResponses = reviews.stream()
          .map(review -> new ReviewDto.Response(
            review.getReviewId(),
            review.getRating(),
            review.getContent(),
            convertToMemberDtoResponse(review.getMember())))
          .collect(Collectors.toList());

        // review rating sum
        double totalRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        totalRating = Math.round(totalRating * 10) / 10.0;
        
        boolean isBookmarked = member != null && isContentBookmarked(member.getId(), content.getContentId());
        
        dto.setBookmarked(isBookmarked);
        dto.setContentId(content.getContentId());
        dto.setFacltNm(content.getFacltNm());
        dto.setLineIntro(content.getLineIntro());
        dto.setIntro(content.getIntro());
        dto.setThemaEnvrnCl(content.getThemaEnvrnCl());
        dto.setMapX(content.getMapX());
        dto.setMapY(content.getMapY());
        dto.setAddr1(content.getAddr1());
        dto.setTel(content.getTel());
        dto.setHomepage(content.getHomepage());
        dto.setResveCl(content.getResveCl());
        dto.setDoNm(content.getDoNm());
        dto.setManageSttus(content.getManageSttus());
        dto.setInduty(content.getInduty());
        dto.setFirstImageUrl(content.getFirstImageUrl());
        dto.setCreatedtime(content.getCreatedtime());
        dto.setModifiedtime(content.getModifiedtime());
        dto.setFeatureNm(content.getFeatureNm());
        dto.setBrazierCl(content.getBrazierCl());
        dto.setGlampInnerFclty(content.getGlampInnerFclty());
        dto.setCaravInnerFclty(content.getCaravInnerFclty());
        dto.setSbrsCl(content.getSbrsCl());
        dto.setAnimalCmgCl(content.getAnimalCmgCl());
        dto.setExprnProgrmAt(content.getExprnProgrmAt());
        dto.setExprnProgrm(content.getExprnProgrm());
        dto.setPosblFcltyCl(content.getPosblFcltyCl());
        dto.setLctCl(content.getLctCl());
        dto.setTotalRating(totalRating);
        dto.setReviews(reviewResponses);
        
        return dto;
    }


    // 고캠핑 API
    public Content postCampingApi(Content content) {
        return campingApiRepository.save(content);
    }

    // 고캠핑 API 특정 키워드 검색
    public List<ContentResponseDto> findKeyword(Long lastContentId,
                                                int keywordId,
                                                AuthorizedMemberDto authorizedMemberDto) {
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        
        Member findMember;
        if (authorizedMemberDto != null) {
            findMember = memberService.findById(authorizedMemberDto.getId());
        } else {
            findMember = null;
        }
        
        List<Content> contents = null;
        switch (keywordId) {
            // 오션뷰
            case 1:
                contents = campingApiRepository.findByLctClContainingAndContentIdGreaterThan("해변", lastContentId, pageRequest);
                break;
            // 피톤치드
            case 2:
                contents = campingApiRepository.findByLctClContainingAndContentIdGreaterThan("숲", lastContentId, pageRequest);
                break;
            // 애견동반
            case 3:
                contents = campingApiRepository.findByAnimalCmgClNotContainingAndContentIdGreaterThan("불가능", lastContentId, pageRequest);
                break;
            // 운동
            case 4:
                contents = campingApiRepository.findBySbrsClContainingAndContentIdGreaterThan("운동", lastContentId, pageRequest);
                break;
            // 물놀이 시간
            case 5:
                contents = campingApiRepository.findByLctClContainingAndContentIdGreaterThan("물놀이", lastContentId, pageRequest);
                break;
            // 단풍
            case 6:
                contents = campingApiRepository.findByThemaEnvrnClContainingAndContentIdGreaterThan("단풍", lastContentId, pageRequest);
                break;
            // 봄꽃여행
            case 7:
                contents = campingApiRepository.findByThemaEnvrnClContainingAndContentIdGreaterThan("봄꽃", lastContentId, pageRequest);
                break;
            // 일몰명소
            case 8:
                contents = campingApiRepository.findByThemaEnvrnClContainingAndContentIdGreaterThan("일몰", lastContentId, pageRequest);
                break;
        }
        return contents.stream().map(content -> convertToContentResponse(content, findMember)).collect(Collectors.toList());
    }

    public List<ContentResponseDto> findCamping(Long lastContentId,
                                                String keyword,
                                                int themaId,
                                                int placeId,
                                                AuthorizedMemberDto authorizedMemberDto) {

        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("createdtime").descending());

        Member findMember;
        if (authorizedMemberDto != null) {
            findMember = memberService.findByEmail(authorizedMemberDto.getEmail());
        } else {
            findMember = null;
        }
        
        List<Content> contents;

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
        if (keyword == null || keyword.isEmpty()) {
            contents = campingApiRepository.findCamping(lastContentId, place, placeSecond, thema, pageRequest);
        }
        else {
            contents = campingApiRepository.findCamping(lastContentId, keyword, place, placeSecond, thema, pageRequest);
        }
        return contents.stream().map(content -> convertToContentResponse(content, findMember)).collect(Collectors.toList());
    }
}