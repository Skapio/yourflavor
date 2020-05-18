package com.skypio.yourflavor.entity;

import lombok.*;

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
    private String description;

}
