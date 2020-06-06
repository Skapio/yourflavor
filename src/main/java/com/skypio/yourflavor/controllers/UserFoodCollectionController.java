package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.dto.request.AddUserFoodCollectionRequest;
import com.skypio.yourflavor.dto.request.UpdateUserFoodCollectionRequest;
import com.skypio.yourflavor.dto.response.UploadFileResponse;
import com.skypio.yourflavor.dto.response.UserFoodCollectionResponse;
import com.skypio.yourflavor.entity.User;
import com.skypio.yourflavor.entity.UserFoodCollection;
import com.skypio.yourflavor.repository.UserFoodCollectionRepository;
import com.skypio.yourflavor.repository.UserRepository;
import com.skypio.yourflavor.security.MyUserDetails;
import com.skypio.yourflavor.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usr-coll")
public class UserFoodCollectionController {

    private final String BASE_PATH = "userFoodCollection";

    private FileStorageService fileStorageService;

    private UserFoodCollectionRepository userFoodCollectionRepository;
    private UserRepository userRepository;

    public UserFoodCollectionController(UserFoodCollectionRepository userFoodCollectionRepository, FileStorageService fileStorageService, UserRepository userRepository)
    {
        this.userFoodCollectionRepository = userFoodCollectionRepository;
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public List<UserFoodCollectionResponse> getAllUserFoodCollection(Principal principal)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails myUserDetails = (MyUserDetails) usernamePasswordAuthenticationToken.getPrincipal();

        Optional<User> optionalUser = userRepository.findById(myUserDetails.getUserId());


        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return userFoodCollectionRepository
                    .findAll()
                    .stream()
                    .map(userFoodCollection -> {
                        Set<String> photos = this.getPhotos(userFoodCollection.getUserFoodCollectionId());

                        return UserFoodCollectionResponse.fromUserFoodCollection(userFoodCollection, photos);
                    }).collect(Collectors.toList());
        } else {
            throw  new RuntimeException("User not exists");
        }
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

    @PostMapping("/update/{id}")
    public UserFoodCollection updateUserFoodCollection(Principal principal, @RequestBody UpdateUserFoodCollectionRequest updateUserFoodCollectionRequest, @PathVariable Integer id)
    {

        Optional<UserFoodCollection> optionalUserFoodCollection = userFoodCollectionRepository.findById(id);

        if (optionalUserFoodCollection.isPresent()) {
            UserFoodCollection userFoodCollection = optionalUserFoodCollection.get();

            userFoodCollection.setCountry(updateUserFoodCollectionRequest.getCountry());
            userFoodCollection.setCity(updateUserFoodCollectionRequest.getCity());
            userFoodCollection.setRestaurantName(updateUserFoodCollectionRequest.getRestaurantName());
            userFoodCollection.setRestaurantAddress(updateUserFoodCollectionRequest.getRestaurantAddress());
            userFoodCollection.setRate(updateUserFoodCollectionRequest.getRate());
            userFoodCollection.setAppFoodCollectionId(updateUserFoodCollectionRequest.getAppFoodCollectionId());

            return userFoodCollectionRepository.save(userFoodCollection);
        } else {
            throw  new RuntimeException("Collection not found for the id "+id);
        }
    }

    @GetMapping("/photo/{userFoodCollectionId}")
    public Set<String> getPhotos(@PathVariable("userFoodCollectionId") Integer userFoodCollectionId)
    {
        return fileStorageService.getFiles(BASE_PATH + "/" + userFoodCollectionId);
    }

    @GetMapping("/photo/{userFoodCollectionId}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable("userFoodCollectionId") Integer userFoodCollectionId, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, BASE_PATH + "/" + userFoodCollectionId);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/photo/{userFoodCollectionId}")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("userFoodCollectionId") Integer userFoodCollectionId) {
        String fileName = fileStorageService.storeFile(file, BASE_PATH + "/" + userFoodCollectionId);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/photo/")
                .path(userFoodCollectionId.toString())
                .path("/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/photos/{userFoodCollectionId}")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @PathVariable("userFoodCollectionId") Integer userFoodCollectionId) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file, userFoodCollectionId))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserFoodCollection(@PathVariable Integer id) {
        Optional<UserFoodCollection> userFoodCollection = userFoodCollectionRepository.findById(id);
        if (userFoodCollection.isPresent())
        {
            userFoodCollectionRepository.delete(userFoodCollection.get());
            return "Collection is deleted with id "+id;
        }
        else
        {
            throw  new RuntimeException("Collection not found for the id "+id);
        }
    }

}
