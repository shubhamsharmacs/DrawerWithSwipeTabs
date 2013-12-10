package com.arcasolutions.api.constant;


public enum ReviewModule {

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