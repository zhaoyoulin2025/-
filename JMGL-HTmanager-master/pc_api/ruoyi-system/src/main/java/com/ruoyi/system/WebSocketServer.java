package com.ruoyi.system;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Message;
import com.ruoyi.system.service.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/wsServer/{uid}")
public class WebSocketServer {


    private static SendMessage sendMessage;

    @Autowired
    public void setSendMessage(SendMessage sendMessage) {
        WebSocketServer.sendMessage = sendMessage;
    }


    /*
     * 使用线程安全的ConcurrentHashMap来存放每个客户端对应的WebSocket对象
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收客户端消息的uid
     */
    private String uid = "";

    /**
     * 连接建立成功调用的方法
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        System.out.println(uid);
        this.session = session;
        this.uid = uid;
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
            //加入到set中
            webSocketMap.put(uid, this);
        } else {
            //加入set中
            webSocketMap.put(uid, this);
        }
        try {
            sendMsg("连接成功");
            //推送数据
            List<Message> messageList = sendMessage.getMessage(uid);
            for (Message message : messageList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("title",message.getTitle());
                map.put("content",message.getContent());
                map.put("userId",uid);
                map.put("id",message.getId());
                String json = JSON.toJSONString(map);
                webSocketMap.get(uid).sendMsg(json);
            }
        } catch (IOException e) {

        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
        }
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
    }

    /**
     * 处理错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     * @param msg
     * @throws IOException
     */
    private void sendMsg(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }


    /**
     * 发送自定义消息
     * @param message
     * @param uid
     * @throws IOException
     */
    public int sendInfo(String message, String uid) throws IOException,EncodeException {

        if (!StringUtils.isEmpty(uid) && webSocketMap.containsKey(uid)) {
            webSocketMap.get(uid).sendMsg(message);
            return 0;
        } else {
            return 1;
        }
    }

}
