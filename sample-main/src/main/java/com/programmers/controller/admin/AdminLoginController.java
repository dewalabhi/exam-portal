package com.programmers.controller.admin;

import com.programmers.configuration.jwt.admin.AdminJwtTokenUtil;
import com.programmers.configuration.jwt.admin.AdminJwtUserDetailsService;
import com.programmers.globalexception.UserSecurityException;
import com.programmers.model.JsonRest;
import com.programmers.model.request.AuthenticationRequest;
import com.programmers.model.response.JwtResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.programmers.common.Constant.JWT_TOKEN_VALIDITY;
import static com.programmers.common.RequestMappingConstant.ADMINISTRATOR_LOGIN;
import static com.programmers.common.RequestMappingConstant.GET_CREDENTIALS;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
public class AdminLoginController {

    private static final Logger logger = LoggerFactory.getLogger(AdminLoginController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminJwtTokenUtil jwtTokenUtil;

    @Autowired
    private AdminJwtUserDetailsService userDetailsService;

    @PostMapping(value = ADMINISTRATOR_LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            logger.info("authentication request uer name - {}", authenticationRequest.getUserName());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
                    authenticationRequest.getPassword()));
            logger.info("user authenticate successfully {}", authenticationRequest.getUserName());
        } catch (DisabledException | BadCredentialsException ex) {
            logger.error("authentication request uer name - {}", authenticationRequest.getUserName());
            logger.error("authentication error ", ex);
            throw new UserSecurityException("Invalid credentials or user disabled");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        logger.info("user token {}", token);
        final Long expirationTime = JWT_TOKEN_VALIDITY * 1000;
        final HashMap<String, String> response = new HashMap<>();
        response.put("JwtResponse", new JwtResponse(token).getToken());
        response.put("ExpireTime", expirationTime.toString());
        return new JsonRest<>(true, response, "", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_CREDENTIALS, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> getCredentials() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        final HashMap<String, String> response = new HashMap<>();
        response.put("username", username);
        return new JsonRest<>(true, response, "", "", HttpStatus.OK);
    }
}
