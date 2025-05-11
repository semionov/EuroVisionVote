package com.rviewer.skeletons.domain.blockchain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Blockchain {
    private List<Block> chain = new ArrayList<>();

    public Blockchain() {
        this.chain = new ArrayList<>();
        this.chain.add(Block.getGenesisBlock());
    }


    public Blockchain(Blockchain other) {
        this.chain = new ArrayList<>();
        for (Block block : other.getChain()) {
            this.chain.add(new Block(block)); // Assumes Block has a copy constructor
        }
    }

    public void addBlock(Block block) {
        // Set the previous hash if this isn't the genesis block
        if (!chain.isEmpty()) {
            block.setPreviousHash(getLastBlock().getHash());
        }

        block.setTimestamp(System.currentTimeMillis());
        block.setHash(Block.generateHashFromBlock(block));
        chain.add(block);
    }

    public boolean isValid() {
        // Validate the integrity of the blockchain
        // Check genesis block
        if (chain.isEmpty()) {
            return true;
        }

        Block genesis = chain.get(0);
        if (!genesis.getHash().equals(Block.generateHashFromBlock(genesis))) {
            return false;
        }

        // Check blocks
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            // Check if current block's hash is valid
            if (!current.getHash().equals(Block.generateHashFromBlock(current))) {
                return false;
            }

            // Check if previous hash matches
            if (!current.getPreviousHash().equals(previous.getHash())) {
                return false;
            }
        }
        return true;
    }

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    public boolean replace(Blockchain newBlockchain) {
        // Basic validation
        if (newBlockchain == null || newBlockchain.getChain().isEmpty()) {
            return false;
        }

        // Check if new chain is valid
        if (!newBlockchain.isValid()) {
            return false;
        }

        // Check if new chain is longer than current
        if (newBlockchain.getChain().size() <= this.chain.size()) {
            return false;
        }

        // Replace the chain
        this.chain = new ArrayList<>(newBlockchain.getChain());
        return true;
    }

    // Getters and setters
    public List<Block> getChain() {
        return new ArrayList<>(chain);
    }

    public void reset() {
        this.chain = new ArrayList<>();
        this.chain.add(Block.getGenesisBlock());
    }
}

