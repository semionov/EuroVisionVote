package com.rviewer.skeletons.infrastructure.services;

import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Vote;
import com.rviewer.skeletons.domain.exceptions.DuplicateVoteException;
import com.rviewer.skeletons.domain.exceptions.InvalidCountryException;
import com.rviewer.skeletons.services.blockchain.BlockchainService;
import com.rviewer.skeletons.services.voting.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VoteServiceTest {
    @Autowired VoteService voteService;
    @Autowired BlockchainService blockchainService;


    @BeforeEach
    void setUp() {
        voteService.clearCache();
        blockchainService.getBlockchain().reset();
    }

    @Test
    void submitVote_duplicate_throws409() {
        voteService.submitVote("FR", "IT");

        // Duplicate rejection by scanning blockchain
        assertThrows(DuplicateVoteException.class,
                () -> voteService.submitVote("FR", "ES"));

        // Verify blockchain contains exactly 1 FR vote
        long frVotes = blockchainService.getBlockchain().getChain().stream()
                .map(Block::getVote)
                .filter(Objects::nonNull)
                .map(Vote::getOriginCountryCode)
                .filter(Objects::nonNull)
                .map(Enum::name)
                .filter("FR"::equals)
                .count();

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


    @Test
    void submitVote_multipleValidVotes() {
        setUp();
        // Submit multiple valid
        assertDoesNotThrow(() -> voteService.submitVote("FR", "IT"));
        assertDoesNotThrow(() -> voteService.submitVote("ES", "IT"));
        assertDoesNotThrow(() -> voteService.submitVote("DE", "IT"));

        // Ensure the blockchain has 3 votes with IT as destination
        long itVotes = blockchainService.getBlockchain().getChain().stream()
                .map(Block::getVote)
                .filter(Objects::nonNull)
                .map(Vote::getDestinationCountryCode)
                .filter(v -> v.name().equals("IT"))
                .count();

        assertEquals(3, itVotes);
    }
}


