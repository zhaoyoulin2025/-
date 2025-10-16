package com.ruoyi.system.utils;



import com.ruoyi.system.domain.Message;
import com.ruoyi.system.service.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class MessageSender {

    private static SendMessage sendMessage;

    @Autowired
    public MessageSender(SendMessage sendMessage) {
        MessageSender.sendMessage = sendMessage;
    }

    public static void send(Message messageDTO) {
        sendMessage.send(messageDTO);
    }



}
