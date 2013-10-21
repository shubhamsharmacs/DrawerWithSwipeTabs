package com.arcasolutions.api.constant;

public enum Resource {

    LISTING("listing"),
    LISTING_CATEGORY("listing_category"),
    EVENT("event"),
    EVENT_CATEGORY("event_category"),
    ARTICLE("article"),
    ARTICLE_CATEGORY("article_category"),
    CLASSIFIED("classified"),
    CLASSIFIED_CATEGORY("classified_category"),
    DEAL("deal"),
    DEAL_CATEGORY("deal_category"),
    REVIEW("review"),
    MODULES_CONF("modulesConf"),
    EDIRECTORY_CONF("getConf");

    private String val;

    private Resource(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
