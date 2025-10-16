package com.ruoyi.domain;

import lombok.Data;

import java.util.Map;

@Data
public class ReviceCheckImageVO {

    private String ToUserName;
    private String FromUserName;
    private Integer CreateTime;
    private String MsgType;
    private String Event;
    private String appid;
    private String trace_id;
    private Integer errcode;

    private Map<String,String> result;

}
