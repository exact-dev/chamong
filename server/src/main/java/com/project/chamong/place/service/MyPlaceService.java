package com.project.chamong.place.service;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.mapper.MyPlaceMapper;
import com.project.chamong.place.repository.MyPlaceRepository;
import com.project.chamong.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPlaceService {
    private final MyPlaceRepository myPlaceRepository;
    private final MemberService memberService;
    private final MyPlaceMapper mapper;
    private final S3Service s3Service;
    
    private String dirName = "myplace_image/";
    
    public List<MyPlaceDto.Response> findMyPlaceByMember(AuthorizedMemberDto authorizedMemberDto){
        Member findMember = memberService.findByEmail(authorizedMemberDto.getEmail());
    
        List<MyPlace> myPlaces = findMember.getMyPlaces();
        
        return mapper.myPlacesToMyPlaceResponse(myPlaces);
    }

    public List<MyPlaceDto.Response> findMyPlaceByIsShared(){
        List<MyPlace> sharedPlaces = myPlaceRepository.findByIsSharedTrue();
        
        return mapper.myPlacesToMyPlaceResponse(sharedPlaces);
    }

    public MyPlaceDto.Response saveMyPlace(MyPlaceDto.Post postDto, AuthorizedMemberDto authorizedMemberDto, MultipartFile placeImg){
        Member findMember = memberService.findByEmail(authorizedMemberDto.getEmail());
        
        postDto.setMyPlaceImg(s3Service.getDefaultCampingImg());
        
        MyPlace myPlace = MyPlace.createMyplace(postDto);
        myPlace.setMember(findMember);
        
        myPlaceRepository.save(myPlace);
        
        return mapper.myPlaceToResponse(myPlace);
    }
    @Transactional
    public MyPlaceDto.Response updateMyPlace(Long id, MyPlaceDto.Patch patchDto, AuthorizedMemberDto authorizedMemberDto, MultipartFile placeImg){
        MyPlace findMyPlace = verifyExistMyPlace(id);
        
        verifyPermission(findMyPlace, authorizedMemberDto);
        
        patchDto.setMyPlaceImg(s3Service.uploadFile(placeImg, dirName));
        
        findMyPlace.updateMyPlace(patchDto);
        
        return mapper.myPlaceToResponse(findMyPlace);
    }
    
    public void deleteMyPlace(Long id, AuthorizedMemberDto authorizedMemberDto){
        MyPlace findMyPlace = verifyExistMyPlace(id);
        verifyPermission(findMyPlace, authorizedMemberDto);
        
        myPlaceRepository.deleteById(id);
    }
    
    public MyPlace verifyExistMyPlace(Long id){
        return myPlaceRepository.findById(id)
          .orElseThrow(() -> new BusinessLogicException(ExceptionCode.My_PLACE_NOT_FOUND));
    }
    
    public void verifyPermission(MyPlace findMyPlace, AuthorizedMemberDto authorizedMemberDto){
    
        if(findMyPlace.getMember().getId() != authorizedMemberDto.getId()){
            throw new BusinessLogicException(ExceptionCode.My_PLACE_UPDATE_OR_DELETE_NO_PERMISSION);
        }
    }

}
