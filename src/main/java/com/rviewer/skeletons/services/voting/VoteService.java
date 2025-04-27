package com.rviewer.skeletons.services.voting;

import com.rviewer.skeletons.domain.blockchain.Country;

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
}
