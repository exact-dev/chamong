package com.project.chamong.community.controller;

import com.project.chamong.community.dto.ArticleDto;
import com.project.chamong.community.mapper.ArticleMapper;
import com.project.chamong.community.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @GetMapping
    public ResponseEntity<List<ArticleDto>> findAll(){
        List<ArticleDto> articleDtos = articleService.findAll();
        return ResponseEntity.ok().body(articleDtos);
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody ArticleDto articleDto){
        Long id = articleService.save(articleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping
    public ResponseEntity update(@PathVariable Long id,@RequestBody ArticleDto articleDto){
        articleService.update(id, articleDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        articleService.delete(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<ArticleDto> findById(@PathVariable Long id){
        ArticleDto articleDto = articleService.findById(id);
        return ResponseEntity.ok().body(articleDto);
    }


}
