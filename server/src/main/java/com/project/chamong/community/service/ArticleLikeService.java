package com.project.chamong.community.service;

import com.project.chamong.community.dto.ArticleLikeDto;
import com.project.chamong.community.entity.ArticleLike;
import com.project.chamong.community.mapper.ArticleLikeMapper;
import com.project.chamong.community.repository.ArticleLikeRepository;
import com.project.chamong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final MemberRepository memberRepository;

    public ArticleLikeDto createLike(ArticleLikeDto articleLikeDto){
        ArticleLike articleLike = ArticleLikeMapper.INSTANCE.toEntity(articleLikeDto);
        articleLike.setMember(memberRepository.getById(articleLikeDto.getMemberId()));
        return ArticleLikeMapper.INSTANCE.toDto(articleLikeRepository.save(articleLike));
    }


}
