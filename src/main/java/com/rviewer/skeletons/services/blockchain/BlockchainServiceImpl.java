package com.rviewer.skeletons.services.blockchain;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;
import com.rviewer.skeletons.domain.blockchain.Vote;
import com.rviewer.skeletons.domain.events.NewBlockEvent;
import com.rviewer.skeletons.services.p2p.P2PWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BlockchainServiceImpl implements BlockchainService {
    private final Blockchain blockchain;
    private final P2PWebSocketClient p2PWebSocketClient;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public BlockchainServiceImpl(Blockchain blockchain, P2PWebSocketClient p2PWebSocketClient, ApplicationEventPublisher eventPublisher) {
        this.blockchain = blockchain;
        blockchain.addBlock(Block.getGenesisBlock());
        this.p2PWebSocketClient = p2PWebSocketClient;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Block addBlock(Vote vote) {
        Block newBlock = new Block();
        newBlock.setVote(vote);
        blockchain.addBlock(newBlock);

        // Broadcast the new block to all connected peers
        String blockData = convertBlockToJson(newBlock);
        p2PWebSocketClient.broadcastToPeers(blockData);
        eventPublisher.publishEvent(new NewBlockEvent(newBlock)); // or receivedBlock


        return newBlock;
    }

    public boolean isValidChain() {
        return blockchain.isValid();
    }

    @Override
    public boolean tryToAddReceivedBlock(Block receivedBlock) {
        Block lastBlock = blockchain.getLastBlock();

        // 1. Check that the previousHash is correct
        if (!receivedBlock.getPreviousHash().equals(lastBlock.getHash())) {
            return false;
        }

        // 2. Check that the hash is valid
        String expectedHash = Block.generateHashFromBlock(receivedBlock);
        if (!receivedBlock.getHash().equals(expectedHash)) {
            return false;
        }

        // 3. Append to local blockchain
        blockchain.getChain().add(receivedBlock);
        eventPublisher.publishEvent(new NewBlockEvent(receivedBlock));
        return true;
    }


    @Override
    public boolean replaceChain(Blockchain newChain) {
        return blockchain.replace(newChain);
    }

    @Override
    public Blockchain getBlockchain() {
        return blockchain;
    }


    // Method to convert a Block to JSON (similar to getBlockchainData)
    private String convertBlockToJson(Block block) {
        // Create a map to represent the block data
        Map<String, Object> blockData = new HashMap<>();
        blockData.put("timestamp", block.getTimestamp());
        blockData.put("previousHash", block.getPreviousHash());
        blockData.put("hash", block.getHash());

        Map<String, String> voteData = new HashMap<>();
        if (block.getVote() != null) {
            voteData.put("originCountryCode", block.getVote().getOrigin().toString());
            voteData.put("destinationCountryCode", block.getVote().getDestination().toString());
        }
        blockData.put("vote", voteData);

        // Convert the block data to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(blockData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error serializing block data";
        }
    }
}

