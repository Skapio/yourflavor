package com.skypio.yourflavor.dto.response;

import com.skypio.yourflavor.entity.UserFoodCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserFoodCollectionResponse {

    private Integer userFoodCollectionId;
    private String country;
    private String city;
    private String restaurantName;
    private String restaurantAddress;
    private Date date;
    private Integer rate;
    private Integer appFoodCollectionId;
    private Set<String> photos;

    public static UserFoodCollectionResponse fromUserFoodCollection(UserFoodCollection userFoodCollection, Set<String> photos)
    {
        return UserFoodCollectionResponse
                .builder()
                .userFoodCollectionId(userFoodCollection.getUserFoodCollectionId())
                .country(userFoodCollection.getCountry())
                .city(userFoodCollection.getCity())
                .restaurantName(userFoodCollection.getRestaurantName())
                .restaurantAddress(userFoodCollection.getRestaurantAddress())
                .date(userFoodCollection.getDate())
                .rate(userFoodCollection.getRate())
                .appFoodCollectionId(userFoodCollection.getAppFoodCollectionId())
                .photos(photos)
                .build();
    }
}
