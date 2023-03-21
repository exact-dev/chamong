package com.project.chamong.place.repository;

import com.project.chamong.place.entity.MyPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyPlaceRepository extends JpaRepository<MyPlace, Long> {
    List<MyPlace> findAllByShared(boolean shared);
}
