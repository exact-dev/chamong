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

  private String profileImg;

  private String about;
  
  private String carName;
  
  private String oilInfo;
  
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "role_member", joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
  @Column(name = "role")
  private List<String> roles = new ArrayList<>();
  
  @OneToMany(mappedBy = "member")
  private List<Article> articles = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<VisitedPlace> visitedPlaces = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<MyPlace> myPlaces = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<ArticleLike> articleLikes = new ArrayList<>();
  
  public void addMyPlace(MyPlace myPlace){
    if(this.myPlaces.contains(myPlace)){
      this.myPlaces.add(myPlace);
    }
    
  }
  @Builder
  public Member(String email, String password, String nickname, String profileImg, String about, String carName, String oilInfo, List<String> roles) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.profileImg = profileImg;
    this.about = about;
    this.carName = carName;
    this.oilInfo = oilInfo;
    this.roles = roles;
  }
  
  
  public static Member createMember(MemberDto.Post postDto){
    return Member.builder()
      .email(postDto.getEmail())
      .nickname(postDto.getNickname())
      .password(postDto.getPassword())
      .profileImg(postDto.getProfileImg())
      .roles(postDto.getRoles())
      .about("자기 소개를 작성 해보세요.")
      .carName("차량 정보 없음")
      .oilInfo("휘발유")
      .build();
  }
  
  public void updateMember(MemberDto.Patch patchDto){
    this.nickname = patchDto.getNickname();
    this.about = patchDto.getAbout();
    this.carName = patchDto.getCarName();
    this.oilInfo = patchDto.getOilInfo();
    this.profileImg = patchDto.getProfileImg();
    
  }
}
