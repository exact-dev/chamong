package com.project.chamong.place.controller;

import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.service.VisitedPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VisitedPlaceController {
    private final VisitedPlaceService visitedPlaceService;

    // 전체 방문 장소 목록 조회
    @GetMapping("/visited")
    public List<VisitedPlaceDto.Response> findAll(){
        return visitedPlaceService.findAll();
    }
    // 단일 방문 장소 조회
    @GetMapping("/visited/{id}")
    public ResponseEntity<VisitedPlaceDto.Response> findById(@PathVariable Long id){
        return visitedPlaceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // 방문 장소 생성
    @PostMapping("/visited")
    public ResponseEntity<VisitedPlaceDto.Response> create(@RequestBody @Valid VisitedPlaceDto.Post postDto){
        VisitedPlaceDto.Response response = visitedPlaceService.create(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 방문 장소 수정
    @PatchMapping("/visited/{id}")
    public ResponseEntity<VisitedPlaceDto.Response> update(@PathVariable Long id, @RequestBody @Valid VisitedPlaceDto.Patch patchDto){
        VisitedPlaceDto.Response response = visitedPlaceService.update(id, patchDto);
        return ResponseEntity.ok(response);
    }
    // 방문 장소 삭제
    @DeleteMapping("/visited/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        visitedPlaceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
