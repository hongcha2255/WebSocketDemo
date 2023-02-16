package com.demo.websocketdemo.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/websocket/{id}")
public class WebSocketService {

    private String id;

    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "id") String id) {
        this.id = id;
        sessionMap.put(id, session);
        log.info("有新的连接 id:{}", id);
    }

    @OnClose
    public void onClose() {
        log.info("断开连接");
    }

    @OnMessage
    public void onMessage(String message) {
        String[] msgArr = message.split(":");
        String toId = msgArr[0];
        String msg = msgArr[1];

        Session session = sessionMap.get(toId);
        session.getAsyncRemote().sendText(msg);

        log.info("msg {} from {} to {}", msg, this.id, toId);
    }


}
