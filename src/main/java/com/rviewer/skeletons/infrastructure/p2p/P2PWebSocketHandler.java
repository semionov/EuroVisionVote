package com.rviewer.skeletons.infrastructure.p2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;
import com.rviewer.skeletons.services.blockchain.BlockchainService;
import com.rviewer.skeletons.services.voting.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class P2PWebSocketHandler extends TextWebSocketHandler {

    private final BlockchainService blockchainService;
    private final VoteService voteService;
    private static final Logger logger = LoggerFactory.getLogger(P2PWebSocketHandler.class);

    public P2PWebSocketHandler(BlockchainService blockchainService, VoteService voteService) {
        this.blockchainService = blockchainService;
        this.voteService = voteService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        ObjectMapper mapper = new ObjectMapper();


        if (payload.trim().startsWith("[")) {
            logger.info("Node received blockchain from peer: " + payload);
            // It's an array of blocks
            List<Block> incomingChain = Arrays.asList(mapper.readValue(payload, Block[].class));
            Blockchain incomingBlockchain = new Blockchain();
            incomingBlockchain.setChain(incomingChain);
            if (blockchainService.replaceBlockchainIfValid(incomingBlockchain)) {
                voteService.loadCache();
                logger.info("Local blockchain was replaced by remote");
            } else {
                logger.info("Remote blockchain was rejected");
            }

        } else {
            // Single block
            logger.info("Node received block from peer: " + payload);
            Block block = mapper.readValue(payload, Block.class);
            blockchainService.tryToAddReceivedBlock(block);
        }
    }

}

