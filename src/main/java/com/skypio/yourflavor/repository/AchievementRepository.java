package com.skypio.yourflavor.repository;

import com.skypio.yourflavor.entity.Achievement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
    List<Achievement> findAll();
}
