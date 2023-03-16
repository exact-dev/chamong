package com.project.chamong.community.service;

import com.project.chamong.community.dto.CommunityDto;
import com.project.chamong.community.entity.Community;
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
        Community post = Community.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .articleImg(request.getArticleImg())
                .build();
        Community save = communityRepository.save(post);
        return save.getCommunityId();
    }

    @Transactional(readOnly = true)
    public Community getPost(Long id){
        Community community = communityRepository.findByCommunityId(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id = "+id));
        return Community.builder()
                .communityId(community.getCommunityId())
                .title(community.getTitle())
                .createdAt(community.getCreatedAt())
                .updateAt(community.getUpdateAt())
                .build();
    }
}
