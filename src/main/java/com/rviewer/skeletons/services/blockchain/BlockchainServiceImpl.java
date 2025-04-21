package com.rviewer.skeletons.services.blockchain;


import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;

public class BlockchainServiceImpl implements BlockchainService {

    private final Blockchain blockchain;

    public BlockchainServiceImpl(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public Block addBlock(String data) {
        // TODO: Block creation and addition
        return null;
    }

    public boolean isValidChain() {
        // TODO: Chain validation
        return false;
    }

    @Override
    public boolean replaceChain(Blockchain newChain) {
        // TODO: Chain replacement
        return false;
    }

    @Override
    public Blockchain getBlockchain() {
        return blockchain;
    }
}

