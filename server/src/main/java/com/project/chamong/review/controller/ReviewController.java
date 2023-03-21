package com.project.chamong.review.controller;

import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.service.CampingApiService;
import com.project.chamong.member.entity.Member;
import com.project.chamong.review.dto.ReviewDto;
import com.project.chamong.review.entity.Review;
import com.project.chamong.review.mapper.ReviewMapper;
import com.project.chamong.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    private final CampingApiService campingApiService;

    private final ReviewMapper mapper;

    public ReviewController(ReviewService reviewService,
                            CampingApiService campingApiService,
                            ReviewMapper mapper) {
        this.reviewService = reviewService;
        this.campingApiService = campingApiService;
        this.mapper = mapper;
    }

    @PostMapping("/{content-id}")
    public ResponseEntity postReview(@PathVariable("content-id") long contentId,
                                     @AuthenticationPrincipal Member member,
                                     @RequestBody ReviewDto.Post requestBody) {
        Content content = campingApiService.findContent(contentId);
        Review review = mapper.reviewPostDtoToReview(requestBody);
        review.setMember(member);
        review.setContents(content);
        Review createdReview = reviewService.createReview(review);

        return new ResponseEntity<>(mapper.reviewResponse(createdReview), HttpStatus.CREATED);
    }
}
