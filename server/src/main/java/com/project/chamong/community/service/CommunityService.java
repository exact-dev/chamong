package com.project.chamong.community.service;

import com.project.chamong.community.dto.CommentDto;
import com.project.chamong.community.dto.CommunityDto;
import com.project.chamong.community.entity.Article;
import com.project.chamong.community.entity.Comment;
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
        Article entity = Article.builder().title("Article item").build();
        communityRepository.save(entity);
        Article saveEntity = communityRepository.findById(entity.getCommunityId()).get();
        return saveEntity.getTitle();
    }

    public static List<CommunityDto> findAll() {
        return communityRepository.findAll().stream().map(CommunityDto::new).collect(Collectors.toList());
    }

    //    public CommunityDto findById(Long id){
//        return new CommunityDto(findCommunityById(id));
//    }
    public static Article findByCommunityId(Long communityId) {
        return communityRepository.findByCommunityId(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id" + communityId));
    }

    public void deleteById(Long id) {
        communityRepository.deleteById(id);
    }

    public static CommunityDto save(CommunityDto communityDto) {
        Article article = communityRepository.save(communityDto.toEntity());
        return new CommunityDto(article);
    }

    //    @Transactional(readOnly = true)
//    public List<ResponseDto> findAll(){
//        return repository.findAll().stream().map(ResponseDto::new).collect(Collectors.toList());
//    }
    public List<Article> create(final Article entity) {
        validate(entity);

        communityRepository.save(entity);
        log.info("Entity Id: {} is saved.", entity.getCommunityId());
        return null;
    }

    public static CommunityDto update(CommunityDto communityDto) {
        Article article = findByCommunityId(communityDto.getCommunityId());
        article.setTitle(article.getTitle());
        article.setContent(article.getContent());
        return new CommunityDto(communityRepository.save(article));
    }

    public CommentDto addComment(Long communityId, CommentDto commentDto) {
        Article article = findByCommunityId(communityId);
        Comment comment = commentDto.toEntity();
        article.addComment(comment);
        communityRepository.save(article);

        return new CommentDto();
    }

    public void deleteComment(Long communityId, Long commentId) {
        Article article = findByCommunityId(communityId);
        Comment comment = article.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id" + commentId));
        article.removeComment(comment);
        communityRepository.save(article);
    }

    private void validate(final Article entity) {
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
//    public List<Article> getAllPosts(){
//        List<Article> posts = communityRepository.findAll();
//        return posts.stream()
//                .map(Article -> Article.builder()
//                                .communityId(Article.getCommunityId())
//                                .title(Article.getTitle())
//                                .content(Article.getContent())
//                )
//                .collect(Collectors.toList());

}
