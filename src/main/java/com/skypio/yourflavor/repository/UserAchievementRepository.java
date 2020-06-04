package com.skypio.yourflavor.repository;

import com.skypio.yourflavor.entity.UserAchievement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAchievementRepository extends CrudRepository<UserAchievement, Integer> {
    List<UserAchievement> findByUserId(Integer userId);
}
