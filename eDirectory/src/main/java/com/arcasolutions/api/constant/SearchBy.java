package com.arcasolutions.api.constant;

public enum SearchBy {
    MAP("map"), KEYWORD("keyword"), CATEGORY("category"), CALENDAR("calendar"), CALENDAR_LIST("calendarList");

    private final String str;

    private SearchBy(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
