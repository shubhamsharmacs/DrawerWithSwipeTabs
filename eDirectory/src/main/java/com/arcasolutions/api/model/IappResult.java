package com.arcasolutions.api.model;

import lombok.Data;

@Data
public class IappResult<T> {

    private boolean success;

    private String message;

    private T result;

    public IappResult() {

    }

    public IappResult(boolean success, String message) {
        setSuccess(success);
        setMessage(message);
    }

}
