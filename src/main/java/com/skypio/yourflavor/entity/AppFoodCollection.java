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
    @Column(columnDefinition = "TEXT")
    private String recipe;
}
