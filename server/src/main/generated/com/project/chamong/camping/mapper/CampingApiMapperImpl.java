package com.project.chamong.camping.mapper;

import com.project.chamong.camping.dto.CampingApiDto;
import com.project.chamong.camping.entity.Content;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-21T16:14:15+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
@Component
public class CampingApiMapperImpl implements CampingApiMapper {

    @Override
    public List<CampingApiDto.response> campingReponses(List<Content> contents) {
        if ( contents == null ) {
            return null;
        }

        List<CampingApiDto.response> list = new ArrayList<CampingApiDto.response>( contents.size() );
        for ( Content content : contents ) {
            list.add( campingReponse( content ) );
        }

        return list;
    }

    @Override
    public CampingApiDto.response campingReponse(Content content) {
        if ( content == null ) {
            return null;
        }

        CampingApiDto.response response = new CampingApiDto.response();

        return response;
    }
}
