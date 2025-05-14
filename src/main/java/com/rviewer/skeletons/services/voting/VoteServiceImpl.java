package com.rviewer.skeletons.services.voting;

import com.rviewer.skeletons.domain.blockchain.Block;
import com.rviewer.skeletons.domain.blockchain.Country;
import com.rviewer.skeletons.domain.blockchain.Vote;
import com.rviewer.skeletons.domain.events.NewBlockEvent;
import com.rviewer.skeletons.domain.exceptions.DuplicateVoteException;
import com.rviewer.skeletons.domain.exceptions.InvalidCountryException;
import com.rviewer.skeletons.domain.responses.VoteResult;
import com.rviewer.skeletons.services.blockchain.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class VoteServiceImpl implements VoteService {
    private final BlockchainService blockchainService;
    private final Set<Country> votedCountriesCache = ConcurrentHashMap.newKeySet();

    @Autowired
    public VoteServiceImpl(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
        initializeCache();
    }

    @Override
    public String submitVote(String originCode, String destinationCode) {
        Country origin = validateCountry(originCode);
        Country destination = validateCountry(destinationCode);

        // Check for duplicates
        if (hasVoted(origin)) {
            throw new DuplicateVoteException(originCode);
        }

        // Add to blockchain
        Vote vote = new Vote(origin, destination, System.currentTimeMillis());
        blockchainService.addBlock(vote);
        votedCountriesCache.add(origin); // Update cache
        return "Vote submitted successfully for " + origin + " to " + destination;
    }

    @Override
    public boolean hasVoted(Country country) {
        // Check cache
        if (votedCountriesCache.contains(country)) {
            System.out.println("TEST clear cahce inside hasVoted : " + votedCountriesCache.isEmpty());
            System.out.println("TEST has voted: " + country + " in cache: " + votedCountriesCache.contains(country));
            return true;
        }

        // Blockchain scan if not in cache
        boolean hasVoted = scanBlockchainForVote(country);
        if (hasVoted) {
            System.out.println("TEST has voted: " + country + " in blockchain: " + hasVoted);
            votedCountriesCache.add(country);
        }
        return hasVoted;
    }

    @Override
    public void clearCache() {
        votedCountriesCache.clear();
        System.out.println("TEST clear cahce: " + votedCountriesCache.isEmpty());
    }

    private boolean scanBlockchainForVote(Country country) {
        return blockchainService.getBlockchain().getChain().stream()
                .skip(1) // Skip genesis block
                .map(Block::getVote)
                .filter(Objects::nonNull)
                .anyMatch(vote -> country.equals(vote.getOrigin()));
    }

    private void initializeCache() {
        blockchainService.getBlockchain().getChain().stream()
                .skip(1) // Skip genesis block
                .map(Block::getVote)
                .filter(Objects::nonNull)
                .map(Vote::getOrigin)
                .forEach(votedCountriesCache::add);
    }

    private Country validateCountry(String countryCode) {
        if (!Country.isValid(countryCode)) {
            throw new InvalidCountryException(countryCode);
        }
        return Country.valueOf(countryCode);
    }

    @Override
    public List<VoteResult> getVoteResults() {
        return blockchainService.getBlockchain().getChain().stream()
                .skip(1) // Skip genesis block
                .map(Block::getVote)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Vote::getDestination,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new VoteResult(
                        entry.getKey().name(),
                        entry.getValue().intValue()
                ))
                .sorted((a, b) -> Integer.compare(b.getVotes(), a.getVotes())) // Descending
                .collect(Collectors.toList());
    }


    @EventListener
    public void handleNewBlockEvent(NewBlockEvent event) {
        Vote vote = event.getBlock().getVote();
        if (vote != null) {
            votedCountriesCache.add(vote.getOrigin());
            //System.out.println("Cache updated from NewBlockEvent for origin: " + vote.getOrigin());
        }
    }

}