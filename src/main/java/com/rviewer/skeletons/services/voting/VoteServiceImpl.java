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
            throw new DuplicateVoteException();
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
            return true;
        }

        // Blockchain scan if not in cache
        boolean hasVoted = scanBlockchainForVote(country);
        if (hasVoted) {
            votedCountriesCache.add(country);
        }
        return hasVoted;
    }

    @Override
    public void loadCache() {
        clearCache();
        initializeCache();
    }

    private void clearCache() {
        votedCountriesCache.clear();
    }

    private boolean scanBlockchainForVote(Country country) {
        return blockchainService.getBlockchain().getChain().stream()
                .skip(1) // Skip genesis block
                .map(Block::getVote)
                .filter(Objects::nonNull)
                .anyMatch(vote -> country.equals(vote.getOriginCountryCode()));
    }

    private void initializeCache() {
        blockchainService.getBlockchain().getChain().stream()
                .skip(1) // Skip genesis block
                .map(Block::getVote)
                .filter(Objects::nonNull)
                .map(Vote::getOriginCountryCode)
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
                        Vote::getDestinationCountryCode,
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
            votedCountriesCache.add(vote.getOriginCountryCode());
        }
    }

}