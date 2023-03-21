package com.project.chamong.community.service;

import com.project.chamong.community.dto.ArticleDto;
import com.project.chamong.community.entity.Article;
import com.project.chamong.community.exception.ArticleNotFoundException;
import com.project.chamong.community.mapper.ArticleMapper;
import com.project.chamong.community.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private static ArticleRepository articleRepository;
    private static ArticleMapper articleMapper;

    public Long save(ArticleDto articleDto){
        Article article = articleMapper.toEntity(articleDto);
        return articleRepository.save(article).getArticleId();
    }

    public ArticleDto update(Long id, ArticleDto articleDto){
        Article article = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException(id));
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setArticleImg(articleDto.getArticleImg());
        article.setUpdateAt(LocalDateTime.now());
        return ArticleMapper.INSTANCE.toDto(articleRepository.save(article));

    }
    public void delete(Long id){
        articleRepository.deleteById(id);
    }

    public void increaseViewCnt(Long id){
        Article article = articleRepository.getById(id);
        article.setViewCnt(article.getViewCnt()+1);
        articleRepository.save(article);
    }
    public ArticleDto findById(Long id){
        Article article = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException(id));
        return articleMapper.toDto(article);
    }
    public List<ArticleDto> findAll(){
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }



}
