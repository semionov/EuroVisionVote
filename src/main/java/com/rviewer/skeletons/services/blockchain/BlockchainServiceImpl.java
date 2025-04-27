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
        Block newBlock = new Block();
        newBlock.setData(data);
        blockchain.addBlock(newBlock);
        return newBlock;
    }

    public boolean isValidChain() {
        return blockchain.isValid();
    }

    @Override
    public boolean replaceChain(Blockchain newChain) {
        return blockchain.replace(newChain);
    }

    @Override
    public Blockchain getBlockchain() {
        return blockchain;
    }
}

