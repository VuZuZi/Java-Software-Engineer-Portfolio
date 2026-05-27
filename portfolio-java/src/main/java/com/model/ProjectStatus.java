package com.model;

public enum ProjectStatus {
    OPEN("Đang mở"),
    IN_PROGRESS("Đang thực hiện"),
    COMPLETED("Đã hoàn thành"),
    CANCELLED("Đã hủy");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}