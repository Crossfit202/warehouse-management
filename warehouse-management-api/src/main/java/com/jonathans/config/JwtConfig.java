package com.jonathans.config;

import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    // Static secret key for temporary testing (32 bytes, base64 encoded)
    private String secretKey = "Ho8JsoaNIz9Zp6yopeR6QcCjTBg1giHnPs1WWpVYAKTeYUl7OpItCvkaL1MExuof";

    public String getSecretKey() {
        return secretKey;
    }
}
