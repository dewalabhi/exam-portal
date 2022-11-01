package com.programmers.service;

import com.programmers.model.JsonRest;
import com.programmers.model.db.User;
import com.programmers.model.response.StatusResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface UserService {

    User save(User user);

    void validateUser(User user);

    Boolean getTokenStatus(String email);

    StatusResponse createUser(User user);

    User getUser(long id);

    List<User> getAllUser();

    Boolean checkUserDetailSaveOrNot(String email);

    Boolean validateEmail(String email);

    ByteArrayInputStream downloadCandidate(List<User> userList) throws IOException;
}
