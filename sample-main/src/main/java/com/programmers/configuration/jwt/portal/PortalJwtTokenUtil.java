package com.programmers.configuration.jwt.portal;

import com.programmers.configuration.jwt.JwtTokenUtilBase;
import com.programmers.repository.ExamRepository;
import com.programmers.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class for Users Token.
 */
@Component
public class PortalJwtTokenUtil extends JwtTokenUtilBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    public String generateToken(String emailId, String userId, String examId, String type) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("UserId", userId);
        claims.put("ExamId", examId);
        claims.put("Type", type);
        return doGenerateToken(claims, emailId);
    }

    // validate token
    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Claims claims = getAllClaimsFromToken(token);
        final String examId = (String) claims.get("ExamId");
        final String userId = (String) claims.get("UserId");
        final String type = (String) claims.get("Type");
        boolean error = type.equals("APT") && !examRepository.findById(Long.parseLong(examId)).isPresent();

        if (!userRepository.findById(Long.parseLong(userId)).isPresent()) {
            error = true;
        }
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !error);
    }
}
