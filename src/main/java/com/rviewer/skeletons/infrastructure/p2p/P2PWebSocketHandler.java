package com.rviewer.skeletons.infrastructure.p2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.services.blockchain.BlockchainService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class P2PWebSocketHandler extends TextWebSocketHandler {

    private final BlockchainService blockchainService;

    public P2PWebSocketHandler(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Parse incoming block from JSON
        String payload = message.getPayload();
        Block receivedBlock = new ObjectMapper().readValue(payload, Block.class);

        // Validate and potentially update chain
        blockchainService.tryToAddReceivedBlock(receivedBlock);
    }
}

