package com.rviewer.skeletons.services.voting;

import com.rviewer.skeletons.domain.blockchain.Country;
import com.rviewer.skeletons.domain.responses.VoteResult;

import java.util.List;

public interface VoteService {

    /**
     * Submit a vote from the origin country to the destination country.
     *
     * @param originCountryCode The country casting the vote (ISO 3166-1 alpha-2).
     * @param destinationCountryCode The country receiving the vote.
     * @return boolean Whether the vote was successfully submitted.
     */
    String submitVote(String originCountryCode, String destinationCountryCode);

    /**
     * Check if a country has already voted.
     *
     * @param country The country code to check.
     * @return boolean Whether the country has voted.
     */
    boolean hasVoted(Country country);



    /**
     * Reset any internally cached voting data.
     *
     * This method is typically used during testing scenarios
     */
    void loadCache();

    /**
     * Returns List of VoteResults
     *
     * @return list List<VoteResult>
     */
    List<VoteResult> getVoteResults();
}
