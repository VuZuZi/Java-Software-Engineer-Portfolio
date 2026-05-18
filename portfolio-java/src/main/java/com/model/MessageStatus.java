package com.model;

public enum MessageStatus {
    UNREAD("Chưa đọc"),
    READ("Đã đọc"),
    REPLIED("Đã trả lời");

    private final String displayName;

    MessageStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}