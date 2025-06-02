package com.rviewer.skeletons.services.blockchain;


import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Blockchain;
import com.rviewer.skeletons.domain.blockchain.Vote;

public interface BlockchainService {

    /**
     * Adds a new block to the blockchain based on the provided vote data.
     *
     * @param vote The vote to be stored in the new block.
     * @return Block The newly added block.
     */
    Block addBlock(Vote vote);

    /**
     * Validates the current blockchain to ensure data integrity.
     *
     * @return boolean True if the chain is valid, false otherwise.
     */
    boolean isValidChain();

    /**
     * Attempts to append a block received from a peer node to the local blockchain.
     *
     * @param receivedBlock The block received from another node.
     * @return boolean True if the block was added, false if it was invalid.
     */
    boolean tryToAddReceivedBlock(Block receivedBlock);

    /**
     * Replaces the current blockchain with a new one if it's longer and valid.
     *
     * @param newChain The new blockchain to consider.
     * @return boolean True if the replacement occurred, false otherwise.
     */
    boolean replaceBlockchainIfValid(Blockchain newChain);

    /**
     * Retrieves the full blockchain.
     *
     * @return Blockchain The current blockchain instance.
     */
    Blockchain getBlockchain();

    /**
     * Returns the latest (most recently added) block in the chain.
     *
     * @return Block The latest block.
     */
    Block getLatestBlock();


    /**
     * Returns the length of the whole blockchain
     *
     * @return int length of blocks
     */
   long getLengthOfChain();

    void resetBlockchain();
}

