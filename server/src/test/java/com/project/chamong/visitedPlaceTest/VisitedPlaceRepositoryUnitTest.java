package com.project.chamong.visitedPlaceTest;

import com.project.chamong.camping.dto.CampingApiDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import com.project.chamong.place.repository.VisitedPlaceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VisitedPlaceRepositoryUnitTest {
  @Autowired
  private VisitedPlaceRepository visitedPlaceRepository;
  
  @Test
  public void Should_Find_All_VisitedPlace_When_VisitedPlace_Exists(){
    //given
    VisitedPlace dummyVisitedPlace1 = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(new Content())
        .build()
    );
    
    VisitedPlace dummyVisitedPlace2 = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(new Content())
        .build()
    );
    
    visitedPlaceRepository.saveAll(List.of(dummyVisitedPlace1, dummyVisitedPlace2));
    
    //when
    List<VisitedPlace> visitedPlaces = visitedPlaceRepository.findAll();
    
    //then
    
    org.assertj.core.api.Assertions.assertThat(visitedPlaces).contains(dummyVisitedPlace1, dummyVisitedPlace2);
  }
  
  @Test
  public void Should_Find_VisitedPlace_By_Id_When_VisitedPlace_Exists(){
    Content content = Content.builder()
      .campingApiPostDto(
        CampingApiDto.Post
          .builder()
          .addr1("전남 담양군 봉산면 탄금길 9-26")
          .facltNm("물놀이시설 잘 갖추어짐 실내 놀이방 실내 독서방  카라반 2동 글램핑 시설이 좋은편")
          .lineIntro("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자")
          .build()
      ).build();
    
    VisitedPlace dummyVisitedPlace = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(content)
        .build()
    );
    
    visitedPlaceRepository.save(dummyVisitedPlace);
    
    VisitedPlace visitedPlace = visitedPlaceRepository.findById(dummyVisitedPlace.getId()).get();
    
    Assertions.assertThat("전남 담양군 봉산면 탄금길 9-26").isEqualTo(visitedPlace.getContent().getAddr1());
    Assertions.assertThat("물놀이시설 잘 갖추어짐 실내 놀이방 실내 독서방  카라반 2동 글램핑 시설이 좋은편").isEqualTo(visitedPlace.getContent().getFacltNm());
    Assertions.assertThat("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자").isEqualTo(visitedPlace.getContent().getLineIntro());
  }
  
  @Test
  public void Should_Delete_VisitedPlace_By_Id_When_VisitedPlace_Exists(){
    //given
    VisitedPlace dummyVisitedPlace = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(new Content())
        .build()
    );
    
    visitedPlaceRepository.save(dummyVisitedPlace);
    
    //when
    visitedPlaceRepository.deleteById(dummyVisitedPlace.getId());
    
    Optional<VisitedPlace> optionalVisitedPlace = visitedPlaceRepository.findById(dummyVisitedPlace.getId());
    
    //then
    org.junit.jupiter.api.Assertions.assertThrows(
      BusinessLogicException.class, () -> optionalVisitedPlace.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MY_PLACE_NOT_FOUND))
      );
  }
}
