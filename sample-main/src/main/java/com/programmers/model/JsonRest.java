package com.programmers.model;

import com.programmers.model.response.JsonRestResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class JsonRest<K, V> extends ResponseEntity<JsonRestResponse<K, V>> {
    private Boolean success;
    private Map<K, V> data;
    private String message;
    private String path;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public JsonRest(Boolean success, Map<K, V> data, String message, String path, HttpStatus httpStatus) {
        super(new JsonRestResponse<>(success, data, message, path), httpStatus);
    }
}

