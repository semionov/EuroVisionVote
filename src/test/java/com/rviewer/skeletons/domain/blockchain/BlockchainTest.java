package com.rviewer.skeletons.domain.blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainTest {

    private Blockchain blockchain;

    @BeforeEach
    void setUp() {
        blockchain = new Blockchain();
    }

    private Block createTestBlock(String origin) {
        Vote vote = new Vote(Country.NL, Country.BE, 17467079);
        Block block = new Block();
        block.setVote(vote);
        return block;
    }

    @Test
    void addBlock_shouldAppendAndLinkCorrectly() {
        Block block = createTestBlock("FR");

        blockchain.addBlock(block);

        assertEquals(2, blockchain.getChain().size());
        Block last = blockchain.getLastBlock();
        assertEquals(block, last);
        assertEquals(block.getPreviousHash(), blockchain.getChain().get(0).getHash());
    }

    @Test
    void isValid_shouldReturnTrueForUnalteredChain() {
        blockchain.addBlock(createTestBlock("FR"));
        blockchain.addBlock(createTestBlock("ES"));

        assertTrue(blockchain.isValid());
    }

    @Test
    void isValid_shouldReturnFalseIfHashIsTampered() {
        blockchain.addBlock(createTestBlock("FR"));
        Block block = blockchain.getChain().get(1);
        block.setHash("tampered");

        assertFalse(blockchain.isValid());
    }

    @Test
    void isValid_shouldReturnFalseIfPreviousHashMismatch() {
        blockchain.addBlock(createTestBlock("FR"));
        Block block = blockchain.getChain().get(1);
        block.setPreviousHash("wrong_previous_hash");

        assertFalse(blockchain.isValid());
    }

    @Test
    void replace_shouldAcceptLongerAndValidChain() {
        Blockchain longerChain = new Blockchain();
        longerChain.addBlock(createTestBlock("FR"));
        longerChain.addBlock(createTestBlock("ES"));

        boolean replaced = blockchain.replace(longerChain);

        assertTrue(replaced);
        assertEquals(3, blockchain.getChain().size());
    }

    @Test
    void replace_shouldRejectShorterChain() {
        Blockchain shorterChain = new Blockchain(); // only genesis

        boolean replaced = blockchain.replace(shorterChain);

        assertFalse(replaced);
        assertEquals(1, blockchain.getChain().size());
    }

    @Test
    void replace_shouldRejectInvalidChain() {
        Blockchain invalidChain = new Blockchain();
        invalidChain.addBlock(createTestBlock("FR"));

        // Tamper the chain
        Block block = invalidChain.getChain().get(1);
        block.setHash("fakeHash");

        boolean replaced = blockchain.replace(invalidChain);

        assertFalse(replaced);
    }
}
