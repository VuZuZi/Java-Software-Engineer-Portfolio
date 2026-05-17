package com.model;

public enum Name {
    FULL_NAME("Đoàn Đình Vũ"),
    DISPLAY_NAME("Vu Doan");

    private final String value;

    Name(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}