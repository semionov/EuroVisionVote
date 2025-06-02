package com.rviewer.skeletons.domain.blockchain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Blockchain {
    private List<Block> chain = new ArrayList<>();

    public Blockchain() {
        this.chain = new ArrayList<>();
    }


    public void addBlock(Block block) {
        block.setTimestamp(System.currentTimeMillis());
        // Set the previous hash if this isn't the genesis block
        if (!chain.isEmpty()) {
            block.setPreviousHash(getLastBlock().getHash());
            block.setHash(Block.generateHashFromBlock(block));
        }

        chain.add(block);
    }

    public boolean isValid() {
        // Validate the integrity of the blockchain
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

    public Block getGenesis() {
        return chain.get(0);
    }

    // Replace the chain
    public boolean replace(Blockchain newBlockchain) {
        this.chain = new ArrayList<>(newBlockchain.getChain());
        return true;
    }

    // Getters and setters
    public List<Block> getChain() {
        return chain;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
    }

    public void reset() {
        this.chain = new ArrayList<>();
        this.chain.add(Block.getGenesisBlock());
    }
}

