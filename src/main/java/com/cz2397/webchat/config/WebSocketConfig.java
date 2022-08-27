package com.cz2397.webchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author: cailin
 * @Date: 2022.8.23
 * @Description: Register @serverEndpoint. (see @serverEndpoint(/websocket) in MyWebSocket
 */

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
}