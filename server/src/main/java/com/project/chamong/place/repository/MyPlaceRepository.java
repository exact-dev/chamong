package com.project.chamong.place.repository;

import com.project.chamong.place.entity.MyPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPlaceRepository extends JpaRepository<MyPlace, Long> {
    List<MyPlace> findByIsSharedTrue();
}
