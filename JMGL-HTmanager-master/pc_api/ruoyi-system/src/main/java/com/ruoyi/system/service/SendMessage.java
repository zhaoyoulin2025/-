package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.Message;

import java.util.List;

public interface SendMessage extends IService<Message> {
    boolean send(Message messageDTO);
    public List<Message> getMessage(String staff );

    public int readMessage(Long id);
}
