package com.skypio.yourflavor.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "user_food_collection")
public class UserFoodCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_food_collection_id")
    private Integer userFoodCollectionId;

    private String country;
    private String city;
    @Column(name = "restaurant_name")
    private String restaurantName;
    @Column(name = "restaurant_address")
    private String restaurantAddress;
    private Date date;
    private Integer rate;
    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "app_food_collection_id")
    private Integer appFoodCollectionId;
}
