package com.ruoyi.system.domain.vo;

public enum MessageType {

    反馈("feedback"),
    任务("task"),
    通知("notify");


    private String tyep;

    MessageType(String tyep) {
        this.tyep = tyep;
    }

    public static String getMessageType(MessageType messageType){
        return messageType.tyep;
    }

}
