package com.ruoyi.system.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;


import java.util.Date;
import java.util.Map;

@Data
@TableName("mini_program_message")
public class Message {
    @TableId(type = IdType.AUTO)
    private  Long id;
    private  String messageType;
    private  String receiverId;
    private  String title;
    private  String content;
    private  int status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date sendTime;
    private  Date readTime;
    private  String extraParams;

    public Message(String receiverId, int status) {
        this.receiverId = receiverId;
        this.status = status;
    }

    public Message() {
    }
}
