package com.project.chamong.place.controller;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import com.project.chamong.place.mapper.VisitedPlaceMapper;
import com.project.chamong.place.service.VisitedPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visited-places")
@Validated
public class VisitedPlaceController {
    private final VisitedPlaceService visitedPlaceService;
    private final VisitedPlaceMapper mapper;
    
    @GetMapping
    public ResponseEntity<?> getVisitedPlaces(){
        List<VisitedPlace> visitedPlaces = visitedPlaceService.findVisitedPlaces();
        List<VisitedPlaceDto.Response> response = mapper.visitedPlacesToResponseDtos(visitedPlaces);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/{content-id}")
    public ResponseEntity<?> postVisitedPlace(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto,
                                              @PathVariable("content-id") @Positive Long contentId){
        VisitedPlace visitedPlace = VisitedPlace.builder().build();
        VisitedPlace saveVisitedPlace = visitedPlaceService.saveVisitedPlace(visitedPlace, contentId, authorizedMemberDto);
    
        VisitedPlaceDto.Response response = mapper.visitedPlaceToResponseDto(saveVisitedPlace);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto,
                                        @PathVariable @Positive Long id) {
        visitedPlaceService.deleteVisitedPlace(id, authorizedMemberDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
