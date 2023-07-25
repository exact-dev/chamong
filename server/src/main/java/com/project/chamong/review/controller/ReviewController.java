package com.project.chamong.review.controller;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.service.CampingApiService;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.review.dto.ReviewDto;
import com.project.chamong.review.entity.Review;
import com.project.chamong.review.mapper.ReviewMapper;
import com.project.chamong.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final CampingApiService campingApiService;

    private final MemberService memberService;

    private final ReviewMapper mapper;

    public ReviewController(ReviewService reviewService,
                            CampingApiService campingApiService,
                            MemberService memberService,
                            ReviewMapper mapper) {
        this.reviewService = reviewService;
        this.campingApiService = campingApiService;
        this.memberService = memberService;
        this.mapper = mapper;
    }

    // 리뷰 추가
    @PostMapping("/{content-id}")
    public ResponseEntity postReview(@PathVariable("content-id") long contentId,
                                     @AuthenticationPrincipal AuthorizedMemberDto authorizedMember,
                                     @RequestBody ReviewDto.Post postDto) {
        Content content = campingApiService.findContent(contentId);
        Review review = mapper.reviewPostDtoToReview(postDto);
        Member findMember = memberService.findById(authorizedMember.getId());
        review.setMember(findMember);
        review.setContents(content);
        Review createdReview = reviewService.createReview(review);
        
        ReviewDto.Response response = mapper.reviewResponse(createdReview);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 리뷰 수정
    @PatchMapping("/{review-id}")
    public ResponseEntity<?> patchReview(@PathVariable("review-id") long reviewId,
                                        @Valid @RequestBody ReviewDto.Patch requestBody){

        Review review = reviewService.updateReview(reviewId, mapper.reviewPatchDtoToReview(requestBody));
        ReviewDto.Response response = mapper.reviewResponse(review);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 리뷰 삭제
    @DeleteMapping("/{review-id}")
    public ResponseEntity<?> deleteReview(@PathVariable("review-id") long reviewId) {
        reviewService.deleteReview(reviewId);
        String result = "리뷰가 정상적으로 삭제되었습니다.";
        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }
}
