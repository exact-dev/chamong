package com.project.chamong.place.service;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.service.CampingApiService;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import com.project.chamong.place.mapper.VisitedPlaceMapper;
import com.project.chamong.place.repository.VisitedPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitedPlaceService {
    private final VisitedPlaceRepository visitedPlaceRepository;
    private final MemberService memberService;
    private final CampingApiService campingApiService;
    private final VisitedPlaceMapper mapper;

    public List< VisitedPlaceDto.Response> findVisitedPlaces(){
        List<VisitedPlace> visitedPlaces = visitedPlaceRepository.findWithContentAll();
        
        return mapper.visitedPlacesToResponseDtos(visitedPlaces);
    }
    
    public VisitedPlaceDto.Response saveVisitedPlace(Long contentId, AuthorizedMemberDto authorizedMemberDto){
        Member findMember = memberService.findById(authorizedMemberDto.getId());
    
        Content findContent = verifyVisitedPlaceExist(findMember, contentId);
    
        VisitedPlaceDto.Post postDto = VisitedPlaceDto.Post.builder().content(findContent).build();
    
        VisitedPlace visitedPlace = VisitedPlace.createVisitedPlace(postDto);
    
        visitedPlace.setMember(findMember);
    
        visitedPlaceRepository.save(visitedPlace);
        
        return mapper.visitedPlaceToResponseDto(visitedPlace);
    }

    public void deleteVisitedPlace(Long id, AuthorizedMemberDto authorizedMemberDto){
        VisitedPlace findVisitedPlace = verifyVisitedPlaceExist(id);
        
        if(findVisitedPlace.getMember().getId().equals(authorizedMemberDto.getId())){
            throw new BusinessLogicException(ExceptionCode.VISITED_PLACE_DELETE_NO_PERMISSION);
        }
        
        visitedPlaceRepository.deleteById(id);
    }
    public Content verifyVisitedPlaceExist(Member member, Long contentId){
        VisitedPlace findVisitedPlace = visitedPlaceRepository.findByMemberIdAndContentId(member.getId(), contentId);
        
        if(findVisitedPlace != null){
            throw new BusinessLogicException(ExceptionCode.VISITED_PLACE_EXISTS);
        }
        
        return campingApiService.findContent(contentId);
    }
    
    public VisitedPlace verifyVisitedPlaceExist(Long id){
        return visitedPlaceRepository.findById(id)
          .orElseThrow(() -> new BusinessLogicException(ExceptionCode.VISITED_PLACE_NOT_FOUND));
    }

}
