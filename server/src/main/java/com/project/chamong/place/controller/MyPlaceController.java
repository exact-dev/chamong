package com.project.chamong.place.controller;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.mapper.MyPlaceMapper;
import com.project.chamong.place.service.MyPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("pick-places")
@Validated
public class MyPlaceController {
    private final MyPlaceService myPlaceService;
    private final MyPlaceMapper mapper;

//    // 전체 장소 목록 조회 - 내 차박지 목록 조회
//    @GetMapping("/pick")
//    public List<MyPlaceDto.Response> findAll(){
//        return myPlaceService.findAll();
//    }
    
    // 공유된 장소 조회 - 공유된 차박지 목록 조회
    @GetMapping
    public ResponseEntity<?> getMyPlace(){
        List<MyPlace> myPlaces = myPlaceService.findMyPlaceByIsShared();
        List<MyPlaceDto.Response> response = mapper.myPlacesToMyPlaceResponse(myPlaces);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 장소 생성
    @PostMapping
    public ResponseEntity<?> postMyPlace(@RequestBody @Valid MyPlaceDto.Post postDto,
                                         @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
        
        MyPlace myPlace = mapper.postDtoToMyPlace(postDto);
        MyPlace saveMyPlace = myPlaceService.saveMyPlace(myPlace, authorizedMemberDto);
        MyPlaceDto.Response response = mapper.myPlaceToResponse(saveMyPlace);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // 장소 수정
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchMyPlace(@RequestBody @Valid MyPlaceDto.Patch patchDto,
                                          @PathVariable @Positive Long id,
                                          @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
        
        MyPlace myPlace = mapper.patchDtoToMyPlace(patchDto);
        MyPlace updateMyPlace = myPlaceService.updateMyPlace(id, myPlace, authorizedMemberDto);
        MyPlaceDto.Response response = mapper.myPlaceToResponse(updateMyPlace);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // 장소 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMyPlace(@PathVariable Long id,
                                           @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
        
        myPlaceService.deleteMyPlace(id, authorizedMemberDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
