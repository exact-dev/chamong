package com.project.chamong.article.service;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.article.mapper.ArticleMapper;
import com.project.chamong.article.repository.ArticleLikeRepository;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.article.repository.CommentRepository;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final CommentRepository commentRepository;
    private final ArticleMapper articleMapper;
    private final MemberRepository memberRepository;

//    public List<ArticleDto.Response> getArticles(String keyword) {
//        return StringUtils.isEmpty(keyword)
//                ? articleRepository.findAll().stream().map(articleMapper::articleResponse).collect(Collectors.toList())
//                : articleRepository.findByTitleContaining(keyword).stream().map(articleMapper::articleResponse).collect(Collectors.toList())
//                .stream().map(article -> {
//                    Member member = memberRepository.findById(article.getMemberId())
//                            .orElseThrow(() -> new IllegalArgumentException("Member not found ID: " + article.getMemberId()));
//                    article.setNickName(member.getNickname());
//                    article.setProfileImg(member.getProfileImg());
//                    article.setOilInfo(member.getOilInfo());
//                    return article;
//                }).collect(Collectors.toList());
//    }

    public List<ArticleDto.Response> getArticles(String keyword) {
        List<ArticleDto.Response> articleResponses = StringUtils.isEmpty(keyword)
                ? articleRepository.findAll().stream().map(articleMapper::articleResponse).collect(Collectors.toList())
                : articleRepository.findByTitleContaining(keyword).stream().map(articleMapper::articleResponse).collect(Collectors.toList());

        for (ArticleDto.Response articleResponse : articleResponses) {
            Member member = memberRepository.findById(articleResponse.getMember().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Member not found ID: " + articleResponse.getMember().getId()));
            articleResponse.setNickName(member.getNickname());
            articleResponse.setProfileImg(member.getProfileImg());
            articleResponse.setOilInfo(member.getOilInfo());
        }

        return articleResponses;
    }


    public ArticleDto.Response getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found ID: " + id));
//        Member member = memberRepository.findById(article.getMember().getId())
//                .orElseThrow(() -> new IllegalArgumentException("Member not found ID: " + article.getMemberId()));
        Member member = article.getMember();
        increaseViewCnt(id);
        ArticleDto.Response response = articleMapper.articleResponse(article);
        response.setNickName(member.getNickname());
        response.setProfileImg(member.getProfileImg());
        response.setOilInfo(member.getOilInfo());
        return response;
    }

    // Article 생성
    public ArticleDto.Response createArticle(ArticleDto.Post postDto) {
        Member member = memberRepository.findById(postDto.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("Member not found with ID: "+ postDto.getMemberId()));
        Article article = Article.createArticle(postDto,member);
        article.setMember(member);
//        article.setMember(memberRepository.findById(postDto.getMemberId())
//                .orElseThrow(()-> new IllegalArgumentException("Member not found ID: "+ postDto.getMemberId())));
        return articleMapper.articleResponse(articleRepository.save(article));
    }

    public ArticleDto.Response updateArticle(Long id, ArticleDto.Patch patchDto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));

        article.update(patchDto);
        return articleMapper.articleResponse(article);
    }

    // Article 삭제
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));

        // 댓글 삭제
        for (Comment comment : article.getComments()) {
            commentRepository.delete(comment);
        }

        articleRepository.delete(article);
    }

    // 조회수
    public void increaseViewCnt(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found ID:" + id));
        article.setViewCnt(article.getViewCnt() + 1);
    }

    @Transactional
    public void likeArticle(Long memberId, Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found ID:" + articleId));
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticle(article);
        articleLike.setMemberId(memberId);
        articleLikeRepository.save(articleLike);
        article.increaseLikeCnt();
    }

    @Transactional
    public void unlikeArticle(Long memberId, Long articleId) {
        ArticleLike articleLike = articleLikeRepository.findByMemberIdAndArticleId(memberId, articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article Like not found with articleId: " + articleId + " and memberId: " + memberId));
        articleLikeRepository.delete(articleLike);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        article.decreaseLikeCnt();
    }

    // 조회수 반환
    public int getArticleViewCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getViewCnt();
    }

    // 좋아요 수 반환
    public int getArticleLikeCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getLikeCnt();
    }

    // 댓글 수 반환
    public int getCommentCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getComments().size();
    }
}
