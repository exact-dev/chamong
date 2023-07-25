package com.project.chamong.member.mapper;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.dto.CommentDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.entity.VisitedPlace;
import com.project.chamong.utils.DeduplicationUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
  @Mapping(target = "about", constant = "자기 소개를 작성 해보세요.")
  @Mapping(target = "carName", constant = "차량 정보를 입력 해보세요.")
  @Mapping(target = "oilInfo", constant = "휘발유")
  @Mapping(target = "profileImg", constant = "img url")
  Member memberPostDtoToMember(MemberDto.Post postDto);
  
  Member memberPatchDtoToMember(MemberDto.Patch patchDto);
  
  MemberDto.Response memberToMemberResponseDto(Member member);
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "roles", ignore = true)
  @Mapping(target = "articles", ignore = true)
  @Mapping(target = "visitedPlaces", ignore = true)
  @Mapping(target = "myPlaces", ignore = true)
  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "articleLikes", ignore = true)
  void memberToMember(Member sourceMember, @MappingTarget Member targetMember);
  
  default MemberDto.MyPageResponse memberToMemberMypageResponse(Member member){
    List<Article> commentedArticles = member.getComments().stream()
      .map(comment -> comment.getArticle())
      .collect(Collectors.toList());
    
    //member 가 하나의 게시글에 남긴 댓글이 여러개일 경우 게시글이 중복되므로 중복을 제거합니다.
    List<Article> distinctCommentedArticles = DeduplicationUtils.removeDuplication(commentedArticles, Article::getId);
    
    List<ArticleLike> memberArticleLikes = member.getArticleLikes();
    
    List<Article> likedArticles = memberArticleLikes.stream()
      .map(articleLike -> articleLike.getArticle())
      .collect(Collectors.toList());
    
  
    return MemberDto.MyPageResponse.builder()
      .memberInfo(memberToMemberResponseDto(member))
      .myPlaceInfos(myPlacesToMyPlaceDtos(member.getMyPlaces()))
      .visitedPlaceInfos(visitedPlacesToVisitedPlaceResponseDtos(member.getVisitedPlaces()))
      .writtenArticleInfos(articlesToArticleResponseDtos(member.getArticles(), memberArticleLikes))
      .commentedArticleInfos(articlesToArticleResponseDtos(distinctCommentedArticles, memberArticleLikes))
      .likedArticleInfos(articlesToArticleResponseDtos(likedArticles, memberArticleLikes))
      .build();
  }
  
  default List<MyPlaceDto.Response> myPlacesToMyPlaceDtos(List<MyPlace> myPlaces){
    return myPlaces.stream()
      .map(myPlace -> MyPlaceDto.Response.builder()
          .id(myPlace.getId())
          .memo(myPlace.getMemo())
          .keywords(myPlace.getKeywords())
          .mapX(myPlace.getMapX())
          .mapY(myPlace.getMapY())
          .createdAt(myPlace.getCreatedAt())
          .updatedAt(myPlace.getUpdatedAt())
          .placeImg(myPlace.getPlaceImg())
          .memberId(myPlace.getMember().getId())
          .isShared(myPlace.getIsShared())
          .build())
      .collect(Collectors.toList());
  }
  
  default List<VisitedPlaceDto.Response> visitedPlacesToVisitedPlaceResponseDtos(List<VisitedPlace> visitedPlaces){
    return visitedPlaces.stream()
      .map(visitedPlace -> VisitedPlaceDto.Response.builder()
        .id(visitedPlace.getId())
        .contentId(visitedPlace.getContent().getContentId())
        .memberId(visitedPlace.getMember().getId())
        .facltNm(visitedPlace.getContent().getFacltNm())
        .lineIntro(visitedPlace.getContent().getLineIntro())
        .addr1(visitedPlace.getContent().getAddr1())
        .firstImageUrl(visitedPlace.getContent().getFirstImageUrl())
        .mapX(visitedPlace.getContent().getMapX())
        .mapY(visitedPlace.getContent().getMapY())
        .createdAt(visitedPlace.getCreatedAt())
        .updatedAt(visitedPlace.getUpdatedAt())
        .build())
      .collect(Collectors.toList());
  }
  
  default List<ArticleDto.Response> articlesToArticleResponseDtos(List<Article> articles, List<ArticleLike> memberArticleLikes){
    System.out.println("articlesToArticleResponseDtos 호출됨");
    
    List<ArticleDto.Response> articleResponses = articles.stream()
      .map(article -> ArticleDto.Response.builder()
        .id(article.getId())
        .title(article.getTitle())
        .content(article.getContent())
        .nickname(article.getMember().getNickname())
        .profileImg(article.getMember().getProfileImg())
        .carName(article.getMember().getCarName())
        .articleImg(article.getArticleImg())
        .isLiked(hasMemberArticleLike(article.getArticleLikes(), memberArticleLikes))
        .memberId(article.getMember().getId())
        .viewCnt(article.getViewCnt())
        .likeCnt(article.getLikeCnt())
        .commentCnt(article.getCommentCnt())
        .comments(commentsToCommentResponseDtos(article.getComments()))
        .createdAt(article.getCreatedAt())
        .updatedAt(article.getUpdatedAt())
        .build())
      .collect(Collectors.toList());
    
    return articleResponses;
  }
  
  default Boolean hasMemberArticleLike(List<ArticleLike> articleArticleLikes, List<ArticleLike> memberArticleLikes){
    
    return memberArticleLikes
      .stream()
      .anyMatch(memberArticleLike -> articleArticleLikes.stream()
        .anyMatch(articleArticleLike -> memberArticleLike.getId() == articleArticleLike.getId()));
  }
  
  default List<CommentDto.Response> commentsToCommentResponseDtos(List<Comment> comments){
    List<CommentDto.Response> commentResponseDtos = comments.stream()
      .map(comment ->
        CommentDto.Response.builder()
          .id(comment.getId())
          .articleId(comment.getArticle().getId())
          .content(comment.getContent())
          .memberId(comment.getMember().getId())
          .nickname(comment.getMember().getNickname())
          .profileImg(comment.getMember().getProfileImg())
          .createdAt(comment.getCreatedAt())
          .updatedAt(comment.getUpdatedAt())
          .build()
      ).collect(Collectors.toList());
    
    return commentResponseDtos;
  }
}
