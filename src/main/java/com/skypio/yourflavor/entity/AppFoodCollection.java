package com.skypio.yourflavor.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "app_food_collection")
public class AppFoodCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_food_collection_id")
    private Integer appFoodCollectionId;

    @Column(name = "dish_name")
    private String dishName;
    @Column(name = "cuisine_type")
    private String cuisineType;
    private String recipe;
    @Column(name = "photo_path")
    private String photoPath;
}
