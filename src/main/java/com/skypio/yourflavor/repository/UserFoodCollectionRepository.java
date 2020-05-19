package com.skypio.yourflavor.repository;

import com.skypio.yourflavor.entity.UserFoodCollection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserFoodCollectionRepository extends CrudRepository<UserFoodCollection, Integer> {
    List<UserFoodCollection> findAll();
    List<UserFoodCollection> findByUserId(Integer user);
}
