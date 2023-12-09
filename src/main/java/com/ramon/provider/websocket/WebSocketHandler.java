package com.ramon.provider.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Procesar el mensaje recibido y enviar una respuesta
        session.sendMessage(new TextMessage("Respuesta del servidor: " + message.getPayload()));
    }
}
