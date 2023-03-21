package com.project.chamong.place.service;

import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import com.project.chamong.place.mapper.VisitedPlaceMapper;
import com.project.chamong.place.repository.VisitedPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitedPlaceService {
    private final VisitedPlaceRepository visitedPlaceRepository;
    private final VisitedPlaceMapper visitedPlaceMapper;
    private final MemberRepository memberRepository;

    public List<VisitedPlaceDto.Response> findAll(){
        List<VisitedPlace> visitedPlaces = visitedPlaceRepository.findAll();
        return visitedPlaces.stream()
                .map(visitedPlaceMapper::visitedPlaceToResponse)
                .collect(Collectors.toList());
    }
    public Optional<VisitedPlaceDto.Response> findById(Long id){
        Optional<VisitedPlace> visitedPlace = visitedPlaceRepository.findById(id);
        return visitedPlace.map(visitedPlaceMapper::visitedPlaceToResponse);
    }
    public VisitedPlaceDto.Response create(VisitedPlaceDto.Post postDto){
        Member member = memberRepository.findById(postDto.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("Member not found with ID: "+ postDto.getMemberId()));
        VisitedPlace visitedPlace = VisitedPlace.createVisitedPlace(postDto, member);
        visitedPlace.setMember(member);

        return visitedPlaceMapper.visitedPlaceToResponse(visitedPlaceRepository.save(visitedPlace));
    }
    public VisitedPlaceDto.Response update(Long id, VisitedPlaceDto.Patch patchDto){
        VisitedPlace visitedPlace = visitedPlaceRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("VisitedPlace not found with ID: "+ id));
        visitedPlace.update(patchDto);
        return visitedPlaceMapper.visitedPlaceToResponse(visitedPlace);
    }
    public void delete(Long id){
        visitedPlaceRepository.deleteById(id);
    }

}
