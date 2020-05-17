package org.hackDefender.websocket;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author vvings
 * @version 2020/5/15 20:31
 */
@EnableWebSocket
@Configuration
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(ApplicationContext context) {
        return new ServerEndpointExporter();
    }

    @Bean
    public ContainerExecWSHandler containerExecWSHandler() {
        return new ContainerExecWSHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(containerExecWSHandler(), "/ws/container/exec.do").addInterceptors(new ContainerExecHandshakeInterceptor()).setAllowedOrigins("*");

    }
}
