package com.project.chamong.review.mapper;

import com.project.chamong.camping.dto.CampingApiDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.review.dto.ReviewDto;
import com.project.chamong.review.entity.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review reviewPostDtoToReview(ReviewDto.Post requestBody);

    Review reviewPatchDtoToReview(ReviewDto.Patch patchDto);

    ReviewDto.Response reviewResponse(Review review);
}
