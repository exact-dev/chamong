package com.project.chamong.review.service;

import com.project.chamong.camping.entity.Content;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.review.entity.Review;
import com.project.chamong.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    // 리뷰 작성
    public Review createReview(Review review){

        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedat(LocalDateTime.now());

        return reviewRepository.save(review);
    }

}
