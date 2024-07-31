package com.mastek.parking.common;

import com.mastek.parking.model.User;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    // Generate a JWT token
    public String generateToken(User user) {
        // Implement JWT token generation logic
        return "token";
    }

    // Invalidate a JWT token
    public void invalidateToken(String token) {
        // Implement token invalidation logic
    }
}
