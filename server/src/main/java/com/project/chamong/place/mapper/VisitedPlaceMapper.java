package com.project.chamong.place.mapper;

import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitedPlaceMapper {
    VisitedPlaceDto postDtoToVisitedPlace(VisitedPlaceDto.Post postDto);
    VisitedPlaceDto.Patch patchDtoToVisitedPlace(VisitedPlaceDto.Patch patchDto);
    VisitedPlaceDto.Response visitedPlaceToResponse(VisitedPlace visitedPlace);
}
