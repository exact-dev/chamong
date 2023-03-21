package com.project.chamong.place.controller;

import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.service.MyPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPlaceController {
    private final MyPlaceService myPlaceService;

    // 전체 장소 목록 조회 - 내 차박지 목록 조회
    @GetMapping("/pick")
    public List<MyPlaceDto.Response> findAll(){
        return myPlaceService.findAll();
    }

    // 단일 장소 조회
    @GetMapping("/pick/{id}")
    public ResponseEntity<MyPlaceDto.Response> findById(@PathVariable Long id){
        return myPlaceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // 공유된 장소 조회 - 공유된 차박지 목록 조회
    @GetMapping("/pick/shared")
    public List<MyPlaceDto.Response> findAllShared(){
        return myPlaceService.findAllShared();
    }

    // 장소 생성
    @PostMapping
    public ResponseEntity<MyPlaceDto.Response> create(@RequestBody @Valid MyPlaceDto.Post postDto){
        MyPlaceDto.Response response = myPlaceService.create(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 장소 수정
    @PatchMapping("/pick/{id}")
    public ResponseEntity<MyPlaceDto.Response> update(@PathVariable Long id, @RequestBody @Valid MyPlaceDto.Patch patchDto){
        MyPlaceDto.Response response = myPlaceService.update(id, patchDto);
        return ResponseEntity.ok(response);
    }
    // 장소 삭제
    @DeleteMapping("/pick/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        myPlaceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
