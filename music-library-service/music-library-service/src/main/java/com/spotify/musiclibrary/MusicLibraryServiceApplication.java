package com.spotify.musiclibrary;

import feign.Retryer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.spotify.musiclibrary.servicefeign")
public class MusicLibraryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicLibraryServiceApplication.class, args);
	}


}
