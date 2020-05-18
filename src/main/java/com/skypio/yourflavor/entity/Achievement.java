package com.skypio.yourflavor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "achievement_id")
    private Integer achievementId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

}
