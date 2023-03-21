package com.project.chamong.place.mapper;

import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MyPlaceMapper {
    @Mapping(target = "shared", ignore = true)
    MyPlace postDtoToMyPlace(MyPlaceDto.Post postDto);
    @Mapping(target = "shared", ignore = true)
    MyPlace patchDtoToMyPlace(MyPlaceDto.Patch patchDto);
    MyPlaceDto.Response myPlaceToResponse(MyPlace myPlace);

}
