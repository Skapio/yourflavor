package com.skypio.yourflavor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserFoodCollectionRequest {

    private String country;
    private String city;
    private String restaurantName;
    private String restaurantAddress;
    private Date date;
    private Integer rate;
    private Integer appFoodCollectionId;
}
