package com.project.chamong.review.mapper;

import com.project.chamong.review.dto.ReviewDto;
import com.project.chamong.review.entity.Review;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-17T00:24:59+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review reviewPostDtoToReview(ReviewDto.Post requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Review review = new Review();

        review.setContent( requestBody.getContent() );
        review.setRating( requestBody.getRating() );

        return review;
    }

    @Override
    public ReviewDto.Response reviewResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDto.Response response = new ReviewDto.Response();

        return response;
    }
}
