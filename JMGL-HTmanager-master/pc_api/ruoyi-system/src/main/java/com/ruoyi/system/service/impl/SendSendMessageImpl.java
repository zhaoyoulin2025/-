package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.WebSocketServer;
import com.ruoyi.system.domain.Message;
import com.ruoyi.system.mapper.MessageMapper;
import com.ruoyi.system.service.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.List;

@Component
public class SendSendMessageImpl extends ServiceImpl<MessageMapper,Message> implements SendMessage {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public List<Message>  getMessage(String staff ) {
        List<Message> messages = messageMapper.selectByReceiverAndStatus(new Message(staff,1));
        return messages;
    }

    @Override
    public int readMessage(Long id) {

        return messageMapper.markAsRead(id);
    }

    public boolean send(Message message) {
        messageMapper.insert(message);
        try {
            String json = JSON.toJSONString(message);
            int i = webSocketServer.sendInfo(json, message.getReceiverId());
            message.setStatus(i);
        }catch (EncodeException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }




}
