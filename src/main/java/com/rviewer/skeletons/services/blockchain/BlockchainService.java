package com.rviewer.skeletons.services.blockchain;


import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;

public interface BlockchainService {

    Block addBlock(String data);

    boolean isValidChain();

    boolean replaceChain(Blockchain newChain);

    Blockchain getBlockchain();
}

