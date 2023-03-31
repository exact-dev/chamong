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
import org.springframework.web.multipart.MultipartFile;

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
    
    // 공유된 장소 조회 - 공유된 차박지 목록 조회
    @GetMapping("/shared")
    public ResponseEntity<?> getMyPlaceIsShared(){
        List<MyPlaceDto.Response> response = myPlaceService.findMyPlaceByIsShared();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // 내가 등록한 차박지만 조회
    @GetMapping("/member")
    public ResponseEntity<?> getMyPlace(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
        List<MyPlaceDto.Response> response = myPlaceService.findMyPlaceByMember(authorizedMemberDto);
    
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 장소 생성
    @PostMapping
    public ResponseEntity<?> postMyPlace(@RequestPart("postMyPlace") @Valid MyPlaceDto.Post postDto,
                                         @RequestPart MultipartFile placeImg,
                                         @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
    
        MyPlaceDto.Response response = myPlaceService.saveMyPlace(postDto, authorizedMemberDto, placeImg);
    
    
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // 장소 수정
    @PatchMapping("/{myPlaceId}")
    public ResponseEntity<?> patchMyPlace(@RequestPart("patchMyPlace") @Valid MyPlaceDto.Patch patchDto,
                                          @RequestPart MultipartFile placeImg,
                                          @PathVariable("myPlaceId") @Positive Long id,
                                          @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
    
        MyPlaceDto.Response response = myPlaceService.updateMyPlace(id, patchDto, authorizedMemberDto, placeImg);
    
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // 장소 삭제
    @DeleteMapping("/{myPlaceId}")
    public ResponseEntity<?> deleteMyPlace(@PathVariable("myPlaceId") @Positive Long id,
                                           @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
        
        myPlaceService.deleteMyPlace(id, authorizedMemberDto);
        String message = "등록한 차박지가 정상적으로 삭제 되었습니다.";
        
        return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
    }
}
