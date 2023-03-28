package com.project.chamong.review.service;

import com.project.chamong.review.entity.Review;
import com.project.chamong.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    // 리뷰 작성
    public Review createReview(Review review){

        return reviewRepository.save(review);
    }

}
