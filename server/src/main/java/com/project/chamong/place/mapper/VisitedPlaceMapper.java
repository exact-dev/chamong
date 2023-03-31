package com.project.chamong.place.mapper;

import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VisitedPlaceMapper {
    @Mapping(source = "content.facltNm", target = "facltNm")
    @Mapping(source = "content.lineIntro", target = "lineIntro")
    @Mapping(source = "content.addr1", target = "addr1")
    @Mapping(source = "content.firstImageUrl", target = "firstImageUrl")
    @Mapping(source = "content.mapX", target = "mapX")
    @Mapping(source = "content.mapY", target = "mapY")
    @Mapping(source = "member.id", target = "memberId")
    VisitedPlaceDto.Response visitedPlaceToResponseDto(VisitedPlace visitedPlace);
    
    default List<VisitedPlaceDto.Response> visitedPlacesToResponseDtos(List<VisitedPlace> visitedPlaces){
        return visitedPlaces.stream()
          .map(visitedPlace -> visitedPlaceToResponseDto(visitedPlace))
          .collect(Collectors.toList());
    }
}
