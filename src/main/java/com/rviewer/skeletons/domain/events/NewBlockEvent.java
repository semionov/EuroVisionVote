package com.rviewer.skeletons.domain.events;

import com.rviewer.skeletons.domain.blockchain.Block;

public class NewBlockEvent {
    private final Block block;

    public NewBlockEvent(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}

