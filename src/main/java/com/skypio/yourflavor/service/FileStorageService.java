package com.skypio.yourflavor.service;

import com.skypio.yourflavor.config.FileStorageProperties;
import com.skypio.yourflavor.exception.FileStorageException;
import com.skypio.yourflavor.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

@Service
public class FileStorageService {

    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {

        this.fileStorageProperties = fileStorageProperties;

    }

    public String storeFile(MultipartFile file, String path) {
        // Normalize file name
        String targetDirectory = fileStorageProperties.getUploadDir() + "/" + path;
        createDirectories(targetDirectory);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(targetDirectory).toAbsolutePath().normalize().resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName, String path) {

        String targetDirectory = fileStorageProperties.getUploadDir() + "/" + path;

        try {
            Path filePath = Paths.get(targetDirectory).toAbsolutePath().normalize().resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public Set<String> getFiles(String path)
    {
        String targetDirectory = fileStorageProperties.getUploadDir() + "/" + path;

        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(targetDirectory))) {
            for (Path filePath : stream) {
                if (!Files.isDirectory(filePath)) {
                    fileList.add(filePath.getFileName()
                            .toString());
                }
            }
        } catch (IOException e) {
            return new HashSet<>();
        }
        return fileList;
    }

    private void createDirectories(String path)
    {
        Path location = Paths.get(path).toAbsolutePath().normalize();

        try {
            Files.createDirectories(location);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
}