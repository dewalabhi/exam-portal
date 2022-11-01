package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class LoginStatusBean {

    private String name;

    private String startDate;

    private HttpStatus code;

    private String message;

    private String path;

    public LoginStatusBean(String name, String startDate, HttpStatus code, String message, String path) {
        super();
        this.name = name;
        this.startDate = startDate;
        this.code = code;
        this.message = message;
        this.path = path;
    }

}
