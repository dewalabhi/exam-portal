package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginBean {

    private String emailId;

    private String password;

    private String deviceNo;

    private String deviceType;

    @Override
    public String toString() {
        return "LoginRequest [emailId=" + emailId + ", password=" + password + "]";
    }

}
