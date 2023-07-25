package com.project.chamong.myPlaceTest;

import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.repository.MyPlaceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MyPlaceRepositoryUnitTest {
  
  @Autowired
  MyPlaceRepository myPlaceRepository;
  
  @Test
  public void Should_FindMyPlace_When_MyPlaceExists(){
    //given
    MyPlace myplace = generateMyPlace();
    
    myPlaceRepository.save(myplace);
    
    //when
    MyPlace findMyPlace = myPlaceRepository.findById(myplace.getId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MY_PLACE_NOT_FOUND));
    
    //then
    Assertions.assertThat(myplace.getId()).isEqualTo(findMyPlace.getId());
    Assertions.assertThat("한강").isEqualTo(findMyPlace.getMemo());
    Assertions.assertThat("#물놀이").isEqualTo(findMyPlace.getKeywords().get(0));
    Assertions.assertThat("#낚시").isEqualTo(findMyPlace.getKeywords().get(1));
    Assertions.assertThat("#강").isEqualTo(findMyPlace.getKeywords().get(2));
    Assertions.assertThat(126.95810162985441).isEqualTo(findMyPlace.getMapX());
    Assertions.assertThat(37.51784937044093).isEqualTo(findMyPlace.getMapY());
  }
  
  @Test
  public void Should_Return_MyPlace_When_Shared_Is_True(){
    //given
    MyPlace myPlace = generateMyPlace();
    myPlace.setIsShared(true);
    
    myPlaceRepository.save(myPlace);
    
    //when
    List<MyPlace> myPlaces = myPlaceRepository.findByIsSharedTrue();
    
    MyPlace filteredMyPlace = myPlaces.stream()
      .filter(myPlaceEntity -> myPlaceEntity.getId() == myPlace.getId())
      .findAny().get();
    
    //then
    Assertions.assertThat(myPlace.getId()).isEqualTo(filteredMyPlace.getId());
    Assertions.assertThat("한강").isEqualTo(filteredMyPlace.getMemo());
    Assertions.assertThat("#물놀이").isEqualTo(filteredMyPlace.getKeywords().get(0));
    Assertions.assertThat("#낚시").isEqualTo(filteredMyPlace.getKeywords().get(1));
    Assertions.assertThat("#강").isEqualTo(filteredMyPlace.getKeywords().get(2));
    Assertions.assertThat(126.95810162985441).isEqualTo(filteredMyPlace.getMapX());
    Assertions.assertThat(37.51784937044093).isEqualTo(filteredMyPlace.getMapY());
  }
  
  @Test
  public void Should_DeleteMyPlace_When_MyPlaceExists(){
    //given
    MyPlace myPlace = generateMyPlace();
    
    myPlaceRepository.save(myPlace);
    
    //when
    myPlaceRepository.deleteById(myPlace.getId());
    
    //then
    org.junit.jupiter.api.Assertions
      .assertThrows(BusinessLogicException.class,
        () -> myPlaceRepository.findById(myPlace.getId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MY_PLACE_NOT_FOUND)));
  }
  
  public MyPlace generateMyPlace(){
    return MyPlace.createMyplace(
      MyPlaceDto.Post
        .builder()
        .memo("한강")
        .keywords(List.of("#물놀이", "#낚시", "#강"))
        .mapX(126.95810162985441)
        .mapY(37.51784937044093)
        .build()
    );
  }
}
