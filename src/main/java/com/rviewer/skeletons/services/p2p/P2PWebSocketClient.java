package com.rviewer.skeletons.services.p2p;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

@Service
public class P2PWebSocketClient extends TextWebSocketHandler {

    private WebSocketClient client = new StandardWebSocketClient();

    // Called when a connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected to peer node: " + session.getUri());
        // send the blockchain data or other initialization requests here
    }

    // Handle incoming messages from the peer node
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        // Process the received block, validate and add it to the blockchain if necessary
    }

    public void connectToPeer(String uri) throws Exception {
        // Connect to the peer node (example: ws://ip:port)
        client.doHandshake(this, uri);
    }

    public void sendBlock(WebSocketSession session, String blockData) throws Exception {
        // Send a block to the peer node
        session.sendMessage(new TextMessage(blockData));
    }
}
