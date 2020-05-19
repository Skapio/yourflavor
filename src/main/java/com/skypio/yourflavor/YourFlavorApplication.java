package com.skypio.yourflavor;

import com.skypio.yourflavor.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class YourFlavorApplication {

	public static void main(String[] args) {
		SpringApplication.run(YourFlavorApplication.class, args);
	}

}
