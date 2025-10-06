package com.spotify.musiclibrary.config;

import com.spotify.musiclibrary.servicefeign.UserClient;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return username -> {
            // Log the actual reason for fallback
            System.err.println("Fallback triggered due to: " + cause);

            // Customize response based on cause
            if (cause instanceof FeignException.NotFound) {
                return "USER_NOT_FOUND";
            } else if (cause instanceof FeignException) {
                return "SERVICE_ERROR";
            } else {
                return "UNKNOWN"; // default fallback
            }
        };
    }
}
