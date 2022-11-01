package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.db.User;
import com.programmers.model.response.StatusResponse;
import com.programmers.repository.UserRepository;
import com.programmers.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static com.programmers.common.Constant.CODE_INTERNAL_SERVER_ERROR;
import static com.programmers.common.Constant.MESSAGE_INTERNAL_SERVER_ERROR;
import static com.programmers.common.RequestMappingConstant.CREATE_USER;
import static com.programmers.common.RequestMappingConstant.DELETE_USER;
import static com.programmers.common.RequestMappingConstant.GET_ALL_USER;
import static com.programmers.common.RequestMappingConstant.GET_USER;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = CREATE_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        StatusResponse statusResponse = new StatusResponse();
        ResponseEntity<Object> response = null;
        try {
            LOGGER.debug("User: {}", user);
            statusResponse = userService.createUser(user);
            response = new ResponseEntity<>(statusResponse, statusResponse.getStatus());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            statusResponse.setCode(CODE_INTERNAL_SERVER_ERROR);
            statusResponse.setMessage(MESSAGE_INTERNAL_SERVER_ERROR);
            response = new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(value = GET_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, User> getUser(@RequestParam(value = "id") long id) {
        final HashMap<String, User> userMap = new HashMap<>();
        userMap.put("user", userService.getUser(id));
        return new JsonRest<>(true, userMap, "", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_ALL_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<User>> getAllUsers() {
        final List<User> userList = userService.getAllUser();
        final HashMap<String, List<User>> userListMap = new HashMap<>();
        userListMap.put("userList", userList);
        if (userList.isEmpty()) {
            return new JsonRest<>(true, userListMap, "User list empty", "", HttpStatus.OK);
        }
        return new JsonRest<>(true, userListMap, "", "", HttpStatus.OK);
    }

    @DeleteMapping(value = DELETE_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> deleteAllUser() {
        final List<User> userList = userService.getAllUser();
        if (userList.isEmpty()) {
            return new JsonRest<>(true, new HashMap<>(), "User list empty", "", HttpStatus.OK);
        }
        userRepository.deleteAll();
        //changes done
        return new JsonRest<>(true, new HashMap<>(), "All Users Deleted Successfully", "", HttpStatus.OK);
    }
}
