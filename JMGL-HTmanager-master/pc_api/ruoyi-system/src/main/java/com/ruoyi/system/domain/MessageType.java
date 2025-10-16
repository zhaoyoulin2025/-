package com.ruoyi.system.domain;

public enum MessageType {
    SYSTEM("system", "系统消息"),
    REMINDER("reminder", "提醒消息"),
    PROGRESS("progress", "进度消息");

    private final String code;
    private final String description;

    MessageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }
}
