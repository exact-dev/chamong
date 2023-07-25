package com.project.chamong.place.mapper;

import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitedPlaceMapper {
    @Mapping(source = "content.facltNm", target = "facltNm")
    @Mapping(source = "content.lineIntro", target = "lineIntro")
    @Mapping(source = "content.addr1", target = "addr1")
    @Mapping(source = "content.firstImageUrl", target = "firstImageUrl")
    @Mapping(source = "content.mapX", target = "mapX")
    @Mapping(source = "content.mapY", target = "mapY")
    @Mapping(source = "content.contentId", target = "contentId")
    @Mapping(source = "member.id", target = "memberId")
    VisitedPlaceDto.Response visitedPlaceToResponseDto(VisitedPlace visitedPlace);
    
    default List<VisitedPlaceDto.Response> visitedPlacesToResponseDtos(List<VisitedPlace> visitedPlaces){
        return visitedPlaces.stream()
          .map(this::visitedPlaceToResponseDto)
          .collect(Collectors.toList());
    }
}
