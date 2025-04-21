package com.rviewer.skeletons.services.voting;

import com.rviewer.skeletons.domain.blockchain.Blockchain;

import java.util.HashSet;
import java.util.Set;

public class VoteServiceImpl implements VoteService {

    private final Blockchain blockchain;
    private final Set<String> votedCountries = new HashSet<>();

    public VoteServiceImpl(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public String submitVote(String origin, String destination) {
        if (hasVoted(origin)) {
            return "Country has already voted.";
        }
        votedCountries.add(origin);
        // TODO: Logic to add the vote to the blockchain
        return "Submitted successfully.";
    }

    @Override
    public boolean hasVoted(String origin) {
        return votedCountries.contains(origin);
    }

    private void markAsVoted(String origin) {
        // TODO: Add country to voted set
    }
}
