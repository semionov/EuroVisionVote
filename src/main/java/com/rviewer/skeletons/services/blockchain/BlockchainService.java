package com.rviewer.skeletons.services.blockchain;


import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;
import com.rviewer.skeletons.domain.blockchain.Vote;

public interface BlockchainService {

    Block addBlock(Vote vote);

    boolean isValidChain();

    boolean replaceChain(Blockchain newChain);

    Blockchain getBlockchain();
}

