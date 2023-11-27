package com.alwyn.groupchat.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebMvcStompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebMvcStompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;
import org.springframework.web.socket.server.support.OriginHandshakeInterceptor;
import org.springframework.web.socket.server.support.WebSocketHandlerMapping;
import org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler;
import org.springframework.web.socket.sockjs.transport.handler.DefaultSockJsService;

@ContextConfiguration(classes = {WebSocketConfig.class})
@ExtendWith(SpringExtension.class)
class WebSocketConfigTest {
    @Autowired
    private WebSocketConfig webSocketConfig;

    /**
     * Test for verifying the behavior of registerStompEndpoints method.
     * Method under test:
     * {@link WebSocketConfig#registerStompEndpoints(StompEndpointRegistry)}
     */
    @Test
    void testRegisterStompEndpoints() {
        MessageChannel clientInboundChannel = mock(MessageChannel.class);
        SubProtocolWebSocketHandler webSocketHandler = new SubProtocolWebSocketHandler(clientInboundChannel,
                new ExecutorSubscribableChannel());

        WebSocketTransportRegistration transportRegistration = new WebSocketTransportRegistration();
        WebMvcStompEndpointRegistry registry = new WebMvcStompEndpointRegistry(webSocketHandler, transportRegistration,
                new ConcurrentTaskScheduler());

        webSocketConfig.registerStompEndpoints(registry);
        AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
        assertTrue(handlerMapping instanceof WebSocketHandlerMapping);
        assertEquals(3,
                ((SubProtocolWebSocketHandler) ((ExceptionWebSocketHandlerDecorator) ((SockJsHttpRequestHandler) ((WebSocketHandlerMapping) handlerMapping)
                        .getUrlMap()
                        .get("/ws/**")).getWebSocketHandler()).getLastHandler()).getProtocolHandlerMap().size());
        assertTrue(
                ((OriginHandshakeInterceptor) ((DefaultSockJsService) ((SockJsHttpRequestHandler) ((WebSocketHandlerMapping) handlerMapping)
                        .getUrlMap()
                        .get("/ws/**")).getSockJsService()).getHandshakeInterceptors().get(0)).getAllowedOrigins().isEmpty());
    }

    /**
     * Test for verifying the behavior of registerStompEndpoints method with a mocked WebMvcStompEndpointRegistry.
     * Method under test:
     * {@link WebSocketConfig#registerStompEndpoints(StompEndpointRegistry)}
     */
    @Test
    void testRegisterStompEndpoints2() {
        WebMvcStompEndpointRegistry registry = mock(WebMvcStompEndpointRegistry.class);
        BinaryWebSocketHandler webSocketHandler = new BinaryWebSocketHandler();
        when(registry.addEndpoint(isA(String[].class))).thenReturn(new WebMvcStompWebSocketEndpointRegistration(
                new String[]{"Paths"}, webSocketHandler, new ConcurrentTaskScheduler()));
        webSocketConfig.registerStompEndpoints(registry);
        verify(registry).addEndpoint(isA(String[].class));
    }

    /**
     * Method under test:
     * {@link WebSocketConfig#configureMessageBroker(MessageBrokerRegistry)}
     */
    @Test
    void testConfigureMessageBroker() {

        WebSocketConfig webSocketConfig = new WebSocketConfig();
        webSocketConfig.configureMessageBroker(
                new MessageBrokerRegistry(new ExecutorSubscribableChannel(), mock(MessageChannel.class)));
        assertTrue(webSocketConfig.configureMessageConverters(null));
    }
}
