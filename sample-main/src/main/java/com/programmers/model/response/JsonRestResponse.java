package com.programmers.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class JsonRestResponse<K, V> {
    private Boolean success;
    private Map<K, V> data;
    private String message;
    private String path;

    public JsonRestResponse(Boolean success, Map<K, V> data, String message, String path) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.path = path;
    }
}
