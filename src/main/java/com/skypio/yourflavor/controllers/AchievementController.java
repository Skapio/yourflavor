package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.dto.response.AchievementsResponse;
import com.skypio.yourflavor.entity.Achievement;
import com.skypio.yourflavor.entity.UserAchievement;
import com.skypio.yourflavor.repository.AchievementRepository;
import com.skypio.yourflavor.repository.UserAchievementRepository;
import com.skypio.yourflavor.security.MyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/achi")
public class AchievementController {

    private AchievementRepository achievementRepository;
    private UserAchievementRepository userAchievementRepository;

    public AchievementController(AchievementRepository achievementRepository, UserAchievementRepository userAchievementRepository)
    {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
    }

    @GetMapping("/list")
    public AchievementsResponse getAllFromAchievement(Principal principal)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails myUserDetails = (MyUserDetails) usernamePasswordAuthenticationToken.getPrincipal();

        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(myUserDetails.getUserId());
        List<Achievement> achievements = achievementRepository.findAll();
        return new AchievementsResponse(achievements, userAchievements);
    }




}
