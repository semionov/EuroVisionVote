package com.rviewer.skeletons.infrastructure.services;

import com.rviewer.skeletons.domain.exceptions.DuplicateVoteException;
import com.rviewer.skeletons.domain.exceptions.InvalidCountryException;
import com.rviewer.skeletons.services.blockchain.BlockchainService;
import com.rviewer.skeletons.services.voting.VoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VoteServiceTest {
    @Autowired VoteService voteService;
    @Autowired BlockchainService blockchainService;

    @Test
    void submitVote_duplicate_throws409() {
        voteService.submitVote("FR", "IT");

        // Duplicate rejection by scanning blockchain
        assertThrows(DuplicateVoteException.class,
                () -> voteService.submitVote("FR", "ES"));

        // Verify blockchain contains exactly 1 FR vote
        long frVotes = blockchainService.getBlockchain().getChain().stream()
                .filter(block -> "FR".equals(block.getVote().getOrigin().name()))
                .count();
        assertEquals(1, frVotes);
    }

    @Test
    void submitVote_invalidOrigin_throws400() {
        Exception exception = assertThrows(
                InvalidCountryException.class,
                () -> voteService.submitVote("XX", "FR")
        );

        assertTrue(exception.getMessage().contains("Valid codes are"));
        assertEquals("XX", ((InvalidCountryException)exception).getCountryCode());
    }
}
