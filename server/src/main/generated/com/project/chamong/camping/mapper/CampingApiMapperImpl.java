package com.project.chamong.camping.mapper;

import com.project.chamong.camping.dto.CampingApiDto;
import com.project.chamong.camping.entity.Content;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

import org.mapstruct.Qualifier;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-16T10:35:31+0900",
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
            list.add( contentToresponse( content ) );
        }

        return list;
    }

    protected CampingApiDto.response contentToresponse(Content content) {
        if ( content == null ) {
            return null;
        }

        String facltNm = null;
        String lineIntro = null;
        String intro = null;
        String themaEnvrnCl = null;
        double mapX = 0.0d;
        double mapY = 0.0d;
        String addr1 = null;
        String tel = null;
        String homepage = null;
        String resveCl = null;
        String doNm = null;
        String manageSttus = null;
        String induty = null;
        String firstImageUrl = null;
        String createdtime = null;
        String modifiedtime = null;
        String featureNm = null;
        String brazierCl = null;
        String glampInnerFclty = null;
        String caravInnerFclty = null;
        String sbrsCl = null;
        String animalCmgCl = null;
        String exprnProgrmAt = null;
        String exprnProgrm = null;
        String posblFcltyCl = null;
        String lctCl = null;

        facltNm = content.getFacltNm();
        lineIntro = content.getLineIntro();
        intro = content.getIntro();
        themaEnvrnCl = content.getThemaEnvrnCl();
        mapX = content.getMapX();
        mapY = content.getMapY();
        addr1 = content.getAddr1();
        tel = content.getTel();
        homepage = content.getHomepage();
        resveCl = content.getResveCl();
        doNm = content.getDoNm();
        manageSttus = content.getManageSttus();
        induty = content.getInduty();
        firstImageUrl = content.getFirstImageUrl();
        createdtime = content.getCreatedtime();
        modifiedtime = content.getModifiedtime();
        featureNm = content.getFeatureNm();
        brazierCl = content.getBrazierCl();
        glampInnerFclty = content.getGlampInnerFclty();
        caravInnerFclty = content.getCaravInnerFclty();
        sbrsCl = content.getSbrsCl();
        animalCmgCl = content.getAnimalCmgCl();
        exprnProgrmAt = content.getExprnProgrmAt();
        exprnProgrm = content.getExprnProgrm();
        posblFcltyCl = content.getPosblFcltyCl();
        lctCl = content.getLctCl();

        CampingApiDto.response response = new CampingApiDto.response( facltNm, lineIntro, intro, themaEnvrnCl, mapX, mapY, addr1, tel, homepage, resveCl, doNm, manageSttus, induty, firstImageUrl, createdtime, modifiedtime, featureNm, brazierCl, glampInnerFclty, caravInnerFclty, sbrsCl, animalCmgCl, exprnProgrmAt, exprnProgrm, posblFcltyCl, lctCl );

        return response;
    }
}
