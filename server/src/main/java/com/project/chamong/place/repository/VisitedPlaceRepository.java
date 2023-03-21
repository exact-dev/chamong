package com.project.chamong.place.repository;

import com.project.chamong.place.entity.VisitedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitedPlaceRepository extends JpaRepository<VisitedPlace, Long> {
}
