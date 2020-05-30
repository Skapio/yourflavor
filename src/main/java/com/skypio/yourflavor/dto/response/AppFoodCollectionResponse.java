package com.skypio.yourflavor.dto.response;

import com.skypio.yourflavor.entity.AppFoodCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AppFoodCollectionResponse {
    private Integer appFoodCollectionId;
    private String dishName;
    private String cuisineType;
    private String recipe;
    private Set<String> photos;

    public static AppFoodCollectionResponse fromAppFoodCollection(AppFoodCollection appFoodCollection, Set<String> photos) {
        return AppFoodCollectionResponse
                .builder()
                .appFoodCollectionId(appFoodCollection.getAppFoodCollectionId())
                .dishName(appFoodCollection.getDishName())
                .cuisineType(appFoodCollection.getCuisineType())
                .recipe(appFoodCollection.getRecipe())
                .photos(photos)
                .build();
    }
}
