package com.project.chamong.place.service;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.mapper.MyPlaceMapper;
import com.project.chamong.place.repository.MyPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPlaceService {
    private final MyPlaceRepository myPlaceRepository;
    private final MemberService memberService;
    private final MyPlaceMapper mapper;

//    public List<MyPlaceDto.Response> findAll(){
//        List<MyPlace> myPlaces = myPlaceRepository.findAll();
//        return myPlaces.stream()
//                .map(myPlaceMapper::myPlaceToResponse)
//                .collect(Collectors.toList());
//    }

    public List<MyPlace> findMyPlaceByIsShared(){
        List<MyPlace> sharedPlaces = myPlaceRepository.findByIsSharedTrue();
        return sharedPlaces;
    }

    public MyPlace saveMyPlace(MyPlace myPlace, AuthorizedMemberDto authorizedMemberDto){
        Member findMember = memberService.findByEmail(authorizedMemberDto.getEmail());
        myPlace.setMember(findMember);
        return myPlaceRepository.save(myPlace);
    }
    @Transactional
    public MyPlace updateMyPlace(Long id, MyPlace myPlace, AuthorizedMemberDto authorizedMemberDto){
        MyPlace findMyPlace = verifyExistMyPlace(id);
        verifyPermission(findMyPlace, authorizedMemberDto);
        
        mapper.myPlaceToMyPlace(myPlace, findMyPlace);
        
        return findMyPlace;
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
