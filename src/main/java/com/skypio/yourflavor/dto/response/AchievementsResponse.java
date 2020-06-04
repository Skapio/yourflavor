package com.skypio.yourflavor.dto.response;

import com.skypio.yourflavor.entity.Achievement;
import com.skypio.yourflavor.entity.UserAchievement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
@Getter
public class AchievementsResponse {
    private List<Achievement> achievements;
    private List<UserAchievement> userAchievements;
}
