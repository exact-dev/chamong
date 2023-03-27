package com.project.chamong.member.entity;

import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.audit.BaseTime;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.entity.VisitedPlace;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String password;

  private String nickname;

  @Column(name = "profile_img")
  private String profileImg;

  private String about;

  @Column(name = "car_name")
  private String carName;

  @Column(name = "oil_info")
  private String oilInfo;
  
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "role_member", joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
  private List<String> roles = new ArrayList<>();
  
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Article> articles;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<VisitedPlace> visitedPlaces;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<MyPlace> myPlaces;
  
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Comment> comments;
  
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<ArticleLike> articleLikes;
  @Builder
  public Member(String email, String password, String nickname, String profileImg, String about, String carName, String oilInfo) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.profileImg = profileImg;
    this.about = about;
    this.carName = carName;
    this.oilInfo = oilInfo;
  }
  
  public static Member createMember(MemberDto.Post postDto){
    return Member.builder()
      .email(postDto.getEmail())
      .nickname(postDto.getNickname())
      .password(postDto.getPassword())
      .about("자기 소개를 작성 해보세요.")
      .carName("차량 정보를 입력 해보세요.")
      .oilInfo("휘발유")
      .profileImg("img url")
      .build();
  }
}
