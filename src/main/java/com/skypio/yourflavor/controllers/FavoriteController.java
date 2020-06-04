package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.entity.AppFoodCollection;
import com.skypio.yourflavor.entity.User;
import com.skypio.yourflavor.repository.AppFoodCollectionRepository;
import com.skypio.yourflavor.repository.UserRepository;
import com.skypio.yourflavor.security.MyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    private UserRepository userRepository;
    private AppFoodCollectionRepository appFoodCollectionRepository;

    public FavoriteController(UserRepository userRepository, AppFoodCollectionRepository appFoodCollectionRepository) {
        this.userRepository = userRepository;
        this.appFoodCollectionRepository = appFoodCollectionRepository;
    }

    @GetMapping
    public Set<AppFoodCollection> getAllForUser(Principal principal)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails myUserDetails = (MyUserDetails) usernamePasswordAuthenticationToken.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(myUserDetails.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return user.getFavoriteAppFoodCollection();
        } else {
            throw  new RuntimeException("User not exists");
        }
    }

    @PostMapping("/{appFoodCollectionId}")
    public void add(Principal principal, @PathVariable Integer appFoodCollectionId)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails myUserDetails = (MyUserDetails) usernamePasswordAuthenticationToken.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(myUserDetails.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<AppFoodCollection> optionalAppFoodCollection = appFoodCollectionRepository.findById(appFoodCollectionId);

            if (optionalAppFoodCollection.isPresent()) {
                user.getFavoriteAppFoodCollection().add(optionalAppFoodCollection.get());
                userRepository.save(user);
            } else {
                throw  new RuntimeException("AppFoodCollection not exists");
            }

        } else {
            throw  new RuntimeException("User not exists");
        }
    }
}
