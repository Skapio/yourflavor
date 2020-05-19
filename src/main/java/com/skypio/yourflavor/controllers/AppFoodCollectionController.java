package com.skypio.yourflavor.controllers;

import com.skypio.yourflavor.entity.AppFoodCollection;
import com.skypio.yourflavor.repository.AppFoodCollectionRepository;
import com.skypio.yourflavor.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/app")
public class AppFoodCollectionController {

    private final String BASE_PATH = "appFoodCollection";

    private FileStorageService fileStorageService;

    private AppFoodCollectionRepository appFoodCollectionRepository;

    public AppFoodCollectionController(AppFoodCollectionRepository appFoodCollectionRepository, FileStorageService fileStorageService)
    {
        this.appFoodCollectionRepository = appFoodCollectionRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/test2")
    public List<AppFoodCollection> getAllFromAppCollection()
    {
        List<AppFoodCollection> aCollections = appFoodCollectionRepository.findAll();

        return aCollections;
    }

    @GetMapping("/photo/{appFoodCollectionId}")
    public Set<String> getPhotos(@PathVariable("appFoodCollectionId") Integer appFoodCollectionId)
    {
        return fileStorageService.getFiles(BASE_PATH + "/" + appFoodCollectionId);
    }

    @GetMapping("/photo/{appFoodCollectionId}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,@PathVariable("appFoodCollectionId") Integer appFoodCollectionId, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, BASE_PATH + "/" + appFoodCollectionId);

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


}
