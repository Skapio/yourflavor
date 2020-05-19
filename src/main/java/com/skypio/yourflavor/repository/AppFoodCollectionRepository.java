package com.skypio.yourflavor.repository;

import com.skypio.yourflavor.entity.AppFoodCollection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppFoodCollectionRepository extends CrudRepository<AppFoodCollection, Integer> {
    List<AppFoodCollection> findAll();
}
