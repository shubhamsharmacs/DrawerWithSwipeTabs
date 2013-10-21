package com.arcasolutions.api.constant;

public enum ModuleName {

    LISTING("listing"), EVENT("event"), CLASSIFIED("classified"), ARTICLE("article");

    private String name;

    private ModuleName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
