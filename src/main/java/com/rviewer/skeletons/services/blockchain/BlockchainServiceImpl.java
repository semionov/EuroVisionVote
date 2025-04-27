package com.rviewer.skeletons.services.blockchain;


import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;
import com.rviewer.skeletons.domain.blockchain.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockchainServiceImpl implements BlockchainService {
    private final Blockchain blockchain;

    @Autowired
    public BlockchainServiceImpl(Blockchain blockchain) {
        this.blockchain = blockchain;
        blockchain.addBlock(Block.getGenesisBlock());
    }

    @Override
    public Block addBlock(Vote vote) {
        Block newBlock = new Block();
        newBlock.setVote(vote);
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

