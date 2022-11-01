package com.programmers.service;

import com.programmers.model.beans.LoginBean;
import com.programmers.model.beans.LoginStatusBean;

import java.text.ParseException;
import java.util.Map;

public interface LoginService {

    LoginStatusBean validateLoginBean(String emailId, String password) throws ParseException;

    Map<String, String> login(LoginBean loginBean);

}
