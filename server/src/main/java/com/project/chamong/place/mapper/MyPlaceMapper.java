package com.project.chamong.place.mapper;

import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MyPlaceMapper {
    MyPlace postDtoToMyPlace(MyPlaceDto.Post postDto);
    MyPlace patchDtoToMyPlace(MyPlaceDto.Patch patchDto);
    @Mapping(source = "member.id", target = "memberId")
    MyPlaceDto.Response myPlaceToResponse(MyPlace myPlace);
    @Mapping(target = "id", ignore = true)
    void myPlaceToMyPlace(MyPlace sourceMyplace, @MappingTarget MyPlace targetMyplace);
    
    default List<MyPlaceDto.Response> myPlacesToMyPlaceResponse (List<MyPlace> myPlaces){
        return myPlaces.stream()
          .map(myPlace -> myPlaceToResponse(myPlace))
          .collect(Collectors.toList());
    }

}
