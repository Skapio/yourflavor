package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.dto.response.AppFoodCollectionResponse;
import com.skypio.yourflavor.entity.AppFoodCollection;
import com.skypio.yourflavor.entity.User;
import com.skypio.yourflavor.repository.AppFoodCollectionRepository;
import com.skypio.yourflavor.repository.UserRepository;
import com.skypio.yourflavor.security.MyUserDetails;
import com.skypio.yourflavor.service.FileStorageService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    private UserRepository userRepository;
    private AppFoodCollectionRepository appFoodCollectionRepository;

    private final String BASE_PATH = "appFoodCollection";

    private FileStorageService fileStorageService;


    public FavoriteController(UserRepository userRepository, AppFoodCollectionRepository appFoodCollectionRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.appFoodCollectionRepository = appFoodCollectionRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<AppFoodCollectionResponse> getAllForUser(Principal principal)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails myUserDetails = (MyUserDetails) usernamePasswordAuthenticationToken.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(myUserDetails.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return user.getFavoriteAppFoodCollection()
                    .stream()
                    .map(appFoodCollection -> {
                        Set<String> photos = this.getPhotos(appFoodCollection.getAppFoodCollectionId());

                        return AppFoodCollectionResponse.fromAppFoodCollection(appFoodCollection, photos);
                    }).collect(Collectors.toList());
        } else {
            throw  new RuntimeException("User not exists");
        }
    }

    public Set<String> getPhotos(@PathVariable("appFoodCollectionId") Integer appFoodCollectionId) {
        return fileStorageService.getFiles(BASE_PATH + "/" + appFoodCollectionId);
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

    @DeleteMapping("/{appFoodCollectionId}")
    public void delete(Principal principal, @PathVariable Integer appFoodCollectionId)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails myUserDetails = (MyUserDetails) usernamePasswordAuthenticationToken.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(myUserDetails.getUserId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<AppFoodCollection> optionalAppFoodCollection = appFoodCollectionRepository.findById(appFoodCollectionId);

            if (optionalAppFoodCollection.isPresent()) {
                user.getFavoriteAppFoodCollection().remove(optionalAppFoodCollection.get());
                userRepository.save(user);
            } else {
                throw  new RuntimeException("AppFoodCollection not exists");
            }

        } else {
            throw  new RuntimeException("User not exists");
        }
    }
}
