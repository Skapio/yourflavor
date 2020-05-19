package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.entity.Achievement;
import com.skypio.yourflavor.repository.AchievementRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achi")
public class AchievementController {

    private AchievementRepository achievementRepository;

    public AchievementController(AchievementRepository achievementRepository)
    {
        this.achievementRepository = achievementRepository;
    }

    @GetMapping("/list")
    public List<Achievement> getAllFromAchievement()
    {
        List<Achievement> achievements = achievementRepository.findAll();
        return achievements;
    }

}
