package com.rviewer.skeletons.infrastructure.p2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.services.blockchain.BlockchainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class P2PWebSocketHandler extends TextWebSocketHandler {

    private final BlockchainService blockchainService;
    private static final Logger logger = LoggerFactory.getLogger(P2PWebSocketHandler.class);

    public P2PWebSocketHandler(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        logger.info("Node received block from peer: " + payload); // ADD THIS LINE
        ObjectMapper mapper = new ObjectMapper();


        if (payload.trim().startsWith("[")) {
            // It's an array of blocks
            Block[] blocks = mapper.readValue(payload, Block[].class);
            for (Block block : blocks) {
                blockchainService.tryToAddReceivedBlock(block);
            }
        } else {
            // Single block
            Block block = mapper.readValue(payload, Block.class);
            blockchainService.tryToAddReceivedBlock(block);
        }
    }

}

