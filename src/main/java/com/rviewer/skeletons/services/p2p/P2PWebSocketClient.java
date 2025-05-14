package com.rviewer.skeletons.services.p2p;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;
import com.rviewer.skeletons.domain.blockchain.Country;
import com.rviewer.skeletons.domain.blockchain.Vote;
import com.rviewer.skeletons.services.blockchain.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class P2PWebSocketClient extends TextWebSocketHandler {

    @Autowired
    private BlockchainService blockchainService;

    private WebSocketClient client = new StandardWebSocketClient();
    private List<WebSocketSession> activeSessions = new ArrayList<>();

    // Called when a connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected to peer node: " + session.getUri());
        activeSessions.add(session);

        String blockchainData = getBlockchainData();
        session.sendMessage(new TextMessage(blockchainData));
    }

    // Handle incoming messages from the peer node
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        // Process the received block, validate and add it to the blockchain if necessary
        handleReceivedBlock(message.getPayload());
    }

    public void connectToPeer(String uri) throws Exception {
        client.doHandshake(this, uri);
    }
    // Send a block to the peer node
    public void sendBlock(WebSocketSession session, String blockData) throws Exception {
        session.sendMessage(new TextMessage(blockData));
    }

    // Broadcast the block to all connected peers
    @Async
    public void broadcastToPeers(String blockData) {
        for (WebSocketSession session : activeSessions) {
            try {
                session.sendMessage(new TextMessage(blockData)); // Send the block data to each active session (peer)
            } catch (Exception e) {
                System.err.println("Error broadcasting block to peer: " + session.getUri());
                e.printStackTrace();
            }
        }
    }

    private String getBlockchainData() {
        Blockchain blockchain = blockchainService.getBlockchain();
        // Convert the blockchain to a list of blocks and then serialize it (for example, to JSON)
        List<Block> chain = blockchain.getChain();
        List<Map<String, Object>> blockchainJson = new ArrayList<>();

        for (Block block : chain) {
            Map<String, Object> blockData = new HashMap<>();
            blockData.put("timestamp", block.getTimestamp());
            blockData.put("previousHash", block.getPreviousHash());
            blockData.put("hash", block.getHash());

            // Include the vote data
            Map<String, String> voteData = new HashMap<>();
            if (block.getVote() != null) {
                voteData.put("originCountryCode", block.getVote().getOrigin().toString());
                voteData.put("destinationCountryCode", block.getVote().getDestination().toString());
            }
            blockData.put("vote", voteData);

            blockchainJson.add(blockData);
        }

        // Convert the list of blocks to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(blockchainJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error serializing blockchain data";
        }
    }

    private void handleReceivedBlock(String blockData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map<String, Object>> blockchainJson = objectMapper.readValue(blockData, List.class);

            for (Map<String, Object> blockDataMap : blockchainJson) {
                long timestamp = ((Number) blockDataMap.get("timestamp")).longValue(); // prevent ClassCastException
                String previousHash = (String) blockDataMap.get("previousHash");
                String hash = (String) blockDataMap.get("hash");

                Map<String, String> voteData = (Map<String, String>) blockDataMap.get("vote");
                String originCountryCode = voteData.get("originCountryCode");
                String destinationCountryCode = voteData.get("destinationCountryCode");

                Block block = new Block();
                block.setTimestamp(timestamp);
                block.setPreviousHash(previousHash);
                block.setHash(hash);
                block.setVote(new Vote(Country.valueOf(originCountryCode), Country.valueOf(destinationCountryCode), timestamp));


                if (!blockchainService.tryToAddReceivedBlock(block)) {
                    System.out.println("Rejected invalid block: " + block);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to process received block data:");
            e.printStackTrace();
        }
    }

}
