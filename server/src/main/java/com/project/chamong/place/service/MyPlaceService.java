package com.project.chamong.place.service;

import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.mapper.MyPlaceMapper;
import com.project.chamong.place.repository.MyPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPlaceService {
    private final MyPlaceRepository myPlaceRepository;
    private final MyPlaceMapper myPlaceMapper;

    public List<MyPlaceDto.Response> findAll(){
        List<MyPlace> myPlaces = myPlaceRepository.findAll();
        return myPlaces.stream()
                .map(myPlaceMapper::myPlaceToResponse)
                .collect(Collectors.toList());
    }

    public Optional<MyPlaceDto.Response> findById(Long id){
        Optional<MyPlace> myPlace = myPlaceRepository.findById(id);
        return myPlace.map(myPlaceMapper::myPlaceToResponse);
    }

    public List<MyPlaceDto.Response> findAllShared(){
        List<MyPlace> sharedPlaces = myPlaceRepository.findAllByShared(true);
        return sharedPlaces.stream()
                .map(myPlaceMapper::myPlaceToResponse)
                .collect(Collectors.toList());
    }

    public MyPlaceDto.Response create(MyPlaceDto.Post postDto){
        MyPlace myPlace = myPlaceRepository.save(MyPlace.createMyPlace(postDto));
        return myPlaceMapper.myPlaceToResponse(myPlace);
    }

    public MyPlaceDto.Response update(Long id, MyPlaceDto.Patch patchDto){
        MyPlace myPlace = myPlaceRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("MyPlace not found with ID: "+ id));
        myPlace.update(patchDto);
        myPlaceRepository.save(myPlace);
        return myPlaceMapper.myPlaceToResponse(myPlace);
    }

    public void deleteById(Long id){
        myPlaceRepository.deleteById(id);
    }

}
