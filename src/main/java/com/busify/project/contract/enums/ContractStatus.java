package com.busify.project.contract.enums;

public enum ContractStatus {
    PENDING("Đang chờ duyệt"),
    ACCEPTED("Đã duyệt"),
    REJECTED("Từ chối"),
    NEED_REVISION("Yêu cầu chỉnh sửa"),
    ACTIVE("Đang hoạt động"),
    EXPIRED("Hết hạn");

    private final String description;

    ContractStatus(String description) {
        this.description = description;

    }

    public String getDescription() {
        return description;
    }
}