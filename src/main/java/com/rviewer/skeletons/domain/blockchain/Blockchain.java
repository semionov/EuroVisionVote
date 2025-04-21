package com.rviewer.skeletons.domain.blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain = new ArrayList<>();

    public Blockchain() {
        //Constructor
    }

    public void addBlock(Block block) {
       // Add block
    }

    public boolean isValid() {
        // Validate the integrity of the blockchain
        return true;
    }

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    public boolean replace(Blockchain newBlockchain) {
        return false;
    }

    // Getters and setters
    public List<Block> getChain() {
        return new ArrayList<>(chain);
    }
}

