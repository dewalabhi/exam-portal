package com.programmers.model.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class StatusBean {

    private HttpStatus code;

    private String message;

    public StatusBean(HttpStatus code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
