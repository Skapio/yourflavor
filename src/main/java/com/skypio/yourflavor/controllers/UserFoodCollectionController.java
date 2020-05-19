package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.dto.request.AddUserFoodCollectionRequest;
import com.skypio.yourflavor.dto.response.UploadFileResponse;
import com.skypio.yourflavor.entity.UserFoodCollection;
import com.skypio.yourflavor.repository.UserFoodCollectionRepository;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usr-coll")
public class UserFoodCollectionController {

    private final String BASE_PATH = "userFoodCollection";

    private FileStorageService fileStorageService;

    private UserFoodCollectionRepository userFoodCollectionRepository;

    public UserFoodCollectionController(UserFoodCollectionRepository userFoodCollectionRepository, FileStorageService fileStorageService)
    {
        this.userFoodCollectionRepository = userFoodCollectionRepository;
        this.fileStorageService = fileStorageService;
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
}
