package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.entity.AppFoodCollection;
import com.skypio.yourflavor.repository.AppFoodCollectionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app")
public class AppFoodCollectionController {


    private AppFoodCollectionRepository appFoodCollectionRepository;

    public AppFoodCollectionController(AppFoodCollectionRepository appFoodCollectionRepository)
    {
        this.appFoodCollectionRepository = appFoodCollectionRepository;
    }

    @GetMapping("/test2")
    public List<AppFoodCollection> getAllFromAppCollection()
    {
        List<AppFoodCollection> aCollections = appFoodCollectionRepository.findAll();

        return aCollections;
    }



}
