package com.ems.notice.quartz.config;


import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

@Configuration
public class WebSocketConfig extends Configurator {

//    private static final String HttpSession = null;
//    /* 修改握手,就是在握手协议建立之前修改其中携带的内容 */
//    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//        /*如果没有监听器,那么这里获取到的HttpSession是null*/
//        StandardSessionFacade ssf = (StandardSessionFacade) request.getHttpSession();
//        if (ssf != null) {
//            javax.servlet.http.HttpSession session = (javax.servlet.http.HttpSession) request.getHttpSession();
//            sec.getUserProperties().put("httpsession", session);
//        }
//        super.modifyHandshake(sec, request, response);
//    }
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        //只有服务器是tomcat的时候才需要配置
        return new ServerEndpointExporter();
    }
}