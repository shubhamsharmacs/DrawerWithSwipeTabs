package com.arcasolutions.api.constant;


import java.io.Serializable;

public enum ReviewModule implements Serializable {

    LISTING("listing"), DEAL("promotion");

    private String val;

    private ReviewModule(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}