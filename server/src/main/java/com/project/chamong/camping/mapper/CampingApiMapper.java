package com.project.chamong.camping.mapper;

import com.project.chamong.camping.dto.CampingApiDto;
import com.project.chamong.camping.entity.Content;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CampingApiMapper {
    List<CampingApiDto.response> campingReponses(List<Content> contents);
}
