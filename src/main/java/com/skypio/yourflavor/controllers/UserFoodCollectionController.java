package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.dto.request.AddUserFoodCollectionRequest;
import com.skypio.yourflavor.entity.UserFoodCollection;
import com.skypio.yourflavor.repository.UserFoodCollectionRepository;
import com.skypio.yourflavor.security.MyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/usr-coll")
public class UserFoodCollectionController {

    private UserFoodCollectionRepository userFoodCollectionRepository;

    public UserFoodCollectionController(UserFoodCollectionRepository userFoodCollectionRepository)
    {
        this.userFoodCollectionRepository = userFoodCollectionRepository;
    }

    @GetMapping("/list")
    public List<UserFoodCollection> getAllUserFoodCollection(Principal principal)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails myUserDetails = (MyUserDetails) usernamePasswordAuthenticationToken.getPrincipal();

        List<UserFoodCollection> uCollections = userFoodCollectionRepository.findByUserId(myUserDetails.getUserId());

        return uCollections;
    }

    @PostMapping("/test2")
    public UserFoodCollection addUserFoodCollection(Principal principal, @RequestBody AddUserFoodCollectionRequest addUserFooCollectionRequest)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails myUserDetails = (MyUserDetails) usernamePasswordAuthenticationToken.getPrincipal();

        UserFoodCollection userFoodCollection = new UserFoodCollection();
        userFoodCollection.setCity(addUserFooCollectionRequest.getCity());
        userFoodCollection.setCountry(addUserFooCollectionRequest.getCountry());
        userFoodCollection.setRestaurantName(addUserFooCollectionRequest.getRestaurantName());
        userFoodCollection.setRestaurantAddress(addUserFooCollectionRequest.getRestaurantAddress());
        userFoodCollection.setDate(new Date());
        userFoodCollection.setRate(addUserFooCollectionRequest.getRate());
        userFoodCollection.setAppFoodCollectionId(addUserFooCollectionRequest.getAppFoodCollectionId());
        userFoodCollection.setUserId(myUserDetails.getUserId());

        return userFoodCollectionRepository.save(userFoodCollection);
    }
}
