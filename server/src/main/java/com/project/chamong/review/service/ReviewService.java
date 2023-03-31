package com.project.chamong.review.service;

import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.review.dto.ReviewDto;
import com.project.chamong.review.entity.Review;
import com.project.chamong.review.mapper.ReviewMapper;
import com.project.chamong.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMapper mapper;

    public ReviewService(ReviewRepository reviewRepository,
                         ReviewMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.mapper = mapper;
    }

    // 리뷰 작성
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    // 리뷰 수정
    @Transactional
    public Review updateReview(Long reviewId, Review review){
        Review findReview = findVerifiedReview(reviewId);

        Optional.ofNullable(review.getRating())
                .ifPresent(rating -> findReview.setRating(rating));
        Optional.ofNullable(review.getContent())
                .ifPresent(content -> findReview.setContent(content));

        return reviewRepository.save(findReview);
    }

    // 리뷰 삭제
    public void deleteReview(long reviewId) {
        Review review = findVerifiedReview(reviewId);
        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public Review findVerifiedReview(long reviewId) {
        Optional<Review> optionalReview =
                reviewRepository.findById(reviewId);
        Review findReview =
                optionalReview.orElseThrow(() ->
        new BusinessLogicException(ExceptionCode.BOOKMARK_NOT_FOUND));
        return findReview;
    }
}
