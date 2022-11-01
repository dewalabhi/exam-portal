package com.programmers.controller.portal;

import com.programmers.common.Constant;
import com.programmers.globalexception.InvalidEmailException;
import com.programmers.model.JsonRest;
import com.programmers.model.db.User;
import com.programmers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

import static com.programmers.common.RequestMappingConstant.PATH_PORTAL;
import static com.programmers.common.RequestMappingConstant.REGISTRATION;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_PORTAL)
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping(value = REGISTRATION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<Object, Object> save(@Valid @RequestBody User user, HttpServletRequest request) {
        final boolean validateEmail = userService.validateEmail(user.getEmailId());
        if (!validateEmail) {
            throw new InvalidEmailException(Constant.INVALID_EMAIL + user.getEmailId());
        }
        final boolean checkUserDetail = userService.checkUserDetailSaveOrNot(user.getEmailId());
        if (checkUserDetail) {
            return new JsonRest<>(true, new HashMap<>(), "User already registered", "/login", HttpStatus.OK);
        }
        userService.save(user);
        return new JsonRest<>(true, new HashMap<>(), "Registration successful", "/login", HttpStatus.CREATED);
    }

}