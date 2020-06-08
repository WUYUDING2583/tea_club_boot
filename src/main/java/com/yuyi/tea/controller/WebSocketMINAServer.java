package com.yuyi.tea.controller;


import com.yuyi.tea.bean.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/mina/{sid}")
@Component
@Slf4j
public class WebSocketMINAServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    protected  int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    protected static CopyOnWriteArraySet<WebSocketMINAServer> webSocketSet = new CopyOnWriteArraySet<WebSocketMINAServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    protected Session session;

    //接收sid
    protected String sid="";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("sid") String sid) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("WebSocketMINAServer！当前在线人数为" + getOnlineCount());
        this.sid=sid;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口"+sid+"的信息:"+message);
        //群发消息
        for (WebSocketMINAServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String text) throws IOException {
        this.session.getBasicRemote().sendText(text);
    }


    /**
     * 群发自定义消息
     * */
    public  void sendInfo(String text,@PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+text);
        for (WebSocketMINAServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(text);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(text);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public  synchronized int getOnlineCount() {
        return onlineCount;
    }

    public  synchronized void addOnlineCount() {
        onlineCount++;
    }

    public  synchronized void subOnlineCount() {
        onlineCount--;
    }
}
