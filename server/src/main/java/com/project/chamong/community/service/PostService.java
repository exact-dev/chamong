package com.project.chamong.community.service;

import com.project.chamong.community.dto.CommunityDto;
import com.project.chamong.community.entity.Article;
import com.project.chamong.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CommunityRepository communityRepository;

    @Transactional
    public Long createPost(CommunityDto request){
        Article post = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .articleImg(request.getArticleImg())
                .build();
        Article save = communityRepository.save(post);
        return save.getCommunityId();
    }

    @Transactional(readOnly = true)
    public Article getPost(Long id){
        Article article = communityRepository.findByCommunityId(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id = "+id));
        return article.builder()
                .communityId(article.getCommunityId())
                .title(article.getTitle())
                .createdAt(article.getCreatedAt())
                .updateAt(article.getUpdateAt())
                .build();
    }
}
