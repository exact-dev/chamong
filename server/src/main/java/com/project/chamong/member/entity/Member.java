package com.project.chamong.member.entity;

import com.project.chamong.article.entity.Article;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.entity.VisitedPlace;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

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

    private String validation;

    @ElementCollection
    @CollectionTable(name = "role_member", joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Article> articles;

    @OneToMany(mappedBy = "member")
    private List<VisitedPlace> visitedPlaces;

    @OneToMany(mappedBy = "member")
    private List<MyPlace> myPlaces;

}
