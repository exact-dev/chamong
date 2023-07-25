package com.project.chamong.place.mapper;

import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyPlaceMapper {
    MyPlace postDtoToMyPlace(MyPlaceDto.Post postDto);
    MyPlace patchDtoToMyPlace(MyPlaceDto.Patch patchDto);
    @Mapping(source = "member.id", target = "memberId")
    MyPlaceDto.Response myPlaceToResponse(MyPlace myPlace);
    @Mapping(target = "id", ignore = true)
    void myPlaceToMyPlace(MyPlace sourceMyplace, @MappingTarget MyPlace targetMyplace);
    
    default List<MyPlaceDto.Response> myPlacesToMyPlaceResponses(List<MyPlace> myPlaces){
        return myPlaces.stream()
          .map(myPlace -> myPlaceToResponse(myPlace))
          .collect(Collectors.toList());
    }
    
}
