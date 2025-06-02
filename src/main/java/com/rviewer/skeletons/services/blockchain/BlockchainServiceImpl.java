package com.rviewer.skeletons.services.blockchain;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;
import com.rviewer.skeletons.domain.blockchain.Vote;
import com.rviewer.skeletons.domain.events.NewBlockEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BlockchainServiceImpl implements BlockchainService {
    private final Blockchain blockchain;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public BlockchainServiceImpl(Blockchain blockchain, ApplicationEventPublisher eventPublisher) {
        this.blockchain = blockchain;
        blockchain.addBlock(Block.getGenesisBlock());
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Block addBlock(Vote vote) {
        Block newBlock = new Block();
        newBlock.setVote(vote);
        blockchain.addBlock(newBlock);

        // Broadcast the new block to all connected peers
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
    public boolean replaceBlockchainIfValid (Blockchain incomingBlockcain) {
        // Basic validation
        if (incomingBlockcain == null || incomingBlockcain.getChain().isEmpty()) {
            return false;
        }

        // Check if new chain is longer than current
        if (incomingBlockcain.getChain().size() <= blockchain.getChain().size()) {
            return false;
        }

        // Validate genesis block
        if (!incomingBlockcain.getGenesis().getHash().equals(blockchain.getGenesis().getHash())) {
            return false;
        }

        // Check integrity of blockchain
        if (!incomingBlockcain.isValid()) {
            return false;
        }

        return blockchain.replace(incomingBlockcain);
    }

    @Override
    public Blockchain getBlockchain() {
        return blockchain;
    }

    @Override
    public Block getLatestBlock() {
        return blockchain.getLastBlock();
    }

    @Override
    public long getLengthOfChain() {
        return blockchain.getChain().size();
    }

    @Override
    public void resetBlockchain() {
        blockchain.reset();
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
            voteData.put("originCountryCode", block.getVote().getOriginCountryCode().toString());
            voteData.put("destinationCountryCode", block.getVote().getDestinationCountryCode().toString());
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

