package com.skypio.yourflavor.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    private String nick;
    private String password;
    private String email;

    @ManyToMany()
    @JoinTable(name = "user_favorite_collection", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "app_food_collection_id"))
    private Set<AppFoodCollection> appFoods;
}
