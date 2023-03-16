package com.project.chamong.community.controller;

import com.project.chamong.community.dto.CommentDto;
import com.project.chamong.community.dto.CommunityDto;
import com.project.chamong.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Autowired vs @RequiredArgsConstructor(이게 더 실용적이라는데)
@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityService service;

    @GetMapping
    public List<CommunityDto> findAll(){
        return service.findAll();
    }

//    @GetMapping
//    public CommunityDto findByCommunityId(@PathVariable Commun){
//        return service.findByCommunityId();
//    }


    @PostMapping
    public CommunityDto create(@RequestBody CommunityDto dto){
        return service.save(dto);
    }

    @PutMapping
    public CommunityDto update(@RequestBody CommunityDto dto){
        return service.update(dto);
    }

    @DeleteMapping
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }


}
