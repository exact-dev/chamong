package com.project.chamong.review.mapper;

import com.project.chamong.review.dto.ReviewDto;
import com.project.chamong.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    Review reviewPostDtoToReview(ReviewDto.Post requestBody);

    Review reviewPatchDtoToReview(ReviewDto.Patch patchDto);

    ReviewDto.Response reviewResponse(Review review);
}
