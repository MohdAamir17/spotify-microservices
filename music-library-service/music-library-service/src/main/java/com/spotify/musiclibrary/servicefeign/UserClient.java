package com.spotify.musiclibrary.servicefeign;

import com.spotify.musiclibrary.config.FeignConfig;
import com.spotify.musiclibrary.config.UserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8081",
        path = "${user.service.context-path}",
        fallbackFactory = UserClientFallbackFactory.class
)
public interface UserClient {
    @GetMapping ("/{username}/role")
    String getUserRole(@PathVariable ("username") String username);
}
