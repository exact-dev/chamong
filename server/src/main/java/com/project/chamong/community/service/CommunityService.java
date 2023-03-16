package com.project.chamong.community.service;

import com.project.chamong.community.dto.CommentDto;
import com.project.chamong.community.dto.CommunityDto;
import com.project.chamong.community.entity.Comment;
import com.project.chamong.community.entity.Community;
import com.project.chamong.community.repository.CommunityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class CommunityService {
    @Autowired
    private static CommunityRepository communityRepository;


    public String testService() {
        Community entity = Community.builder().title("Community item").build();
        communityRepository.save(entity);
        Community saveEntity = communityRepository.findById(entity.getCommunityId()).get();
        return saveEntity.getTitle();
    }

    public static List<CommunityDto> findAll() {
        return communityRepository.findAll().stream().map(CommunityDto::new).collect(Collectors.toList());
    }

    //    public CommunityDto findById(Long id){
//        return new CommunityDto(findCommunityById(id));
//    }
    public static Community findByCommunityId(Long communityId) {
        return communityRepository.findByCommunityId(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id" + communityId));
    }

    public void deleteById(Long id) {
        communityRepository.deleteById(id);
    }

    public static CommunityDto save(CommunityDto communityDto) {
        Community community = communityRepository.save(communityDto.toEntity());
        return new CommunityDto(community);
    }

    //    @Transactional(readOnly = true)
//    public List<ResponseDto> findAll(){
//        return repository.findAll().stream().map(ResponseDto::new).collect(Collectors.toList());
//    }
    public List<Community> create(final Community entity) {
        validate(entity);

        communityRepository.save(entity);
        log.info("Entity Id: {} is saved.", entity.getCommunityId());
        return null;
    }

    public static CommunityDto update(CommunityDto communityDto) {
        Community community = findByCommunityId(communityDto.getCommunityId());
        community.setTitle(community.getTitle());
        community.setContent(community.getContent());
        return new CommunityDto(communityRepository.save(community));
    }

    public CommentDto addComment(Long communityId, CommentDto commentDto) {
        Community community = findByCommunityId(communityId);
        Comment comment = commentDto.toEntity();
        community.addComment(comment);
        communityRepository.save(community);

        return new CommentDto();
    }

    public void deleteComment(Long communityId, Long commentId) {
        Community community = findByCommunityId(communityId);
        Comment comment = community.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id" + commentId));
        community.removeComment(comment);
        communityRepository.save(community);
    }

    private void validate(final Community entity) {
        if (entity == null) {
            log.warn("Entity can't be null");
            throw new RuntimeException("Entity can't be null");
        }

        if (entity.getCommunityId() == null) {
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }


//    @Transactional
//    public List<Community> getAllPosts(){
//        List<Community> posts = communityRepository.findAll();
//        return posts.stream()
//                .map(community -> Community.builder()
//                                .communityId(community.getCommunityId())
//                                .title(community.getTitle())
//                                .content(community.getContent())
//                )
//                .collect(Collectors.toList());

}
