package com.ems.notice.quartz.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ems.notice.quartz.constant.NoticeType;
import com.ems.notice.quartz.mapper.NoticeMapper;
import com.ems.notice.quartz.model.entity.Notice;
import com.ems.notice.quartz.service.NoticeService;
import com.ems.notice.quartz.service.TaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
@Service
@ServerEndpoint("/api/websocket/{uid}")
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收uid
    private String uid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        this.session = session;
        webSocketSet.add(this);     // 加入set中
        this.uid = uid;
        addOnlineCount();           // 在线数加1

        try {
//            sendMessage("conn_success");
            log.info("有新客户端开始监听,uid=" + uid + ",当前在线人数为:" + getOnlineCount());
        } catch (Exception e) {
            log.error("websocket IO Exception");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  // 从set中删除
        subOnlineCount();              // 在线数减1
        // 断开连接情况下，更新主板占用情况为释放
        log.info("释放的uid=" + uid + "的客户端");
        releaseResource();
    }

    private void releaseResource() {
        // 这里写释放资源和要处理的业务
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自客户端 uid=" + uid + " 的信息:" + message);
        // 群发消息
        HashSet<String> uids = new HashSet<>();
        for (WebSocketServer item : webSocketSet) {
            if (!this.uid.equals(item.uid)){
                uids.add(item.uid);
            }
        }
        try {
            sendMessage("客户端 " + this.uid + "发布消息：" + message, uids);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误回调
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error(session.getBasicRemote() + "客户端发生错误");
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
    public static void sendMessage(String message, HashSet<String> touids) throws IOException {
        log.info("推送消息到客户端 " + touids + "，推送内容:" + message);

        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给传入的uid，为null则全部推送
                if (touids.size() == 0) {
                    item.sendMessage(message);
                } else if (touids.contains(item.uid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static boolean sendMessage(String message, String uid) throws IOException {
        log.info("推送消息到客户端 " + uid + "，推送内容:" + message);
        boolean flag = false;
        for (WebSocketServer item : webSocketSet) {
            try {
                if (uid.equals(item.uid)) {
                    item.sendMessage(message);
                    flag = true;
                }
                if (flag){
                    break;
                }
            } catch (IOException e) {
                continue;
            }
        }
        return flag;
    }

    /**
     * 实现服务器主动推送消息到 指定客户端
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 获取当前在线人数
     *
     * @return
     */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 当前在线人数 +1
     *
     * @return
     */
    public static void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    /**
     * 当前在线人数 -1
     *
     * @return
     */
    public static void subOnlineCount() {
        onlineCount.getAndDecrement();
    }

    /**
     * 获取当前在线客户端对应的WebSocket对象
     *
     * @return
     */
    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }
}