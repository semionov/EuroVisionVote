package com.rviewer.skeletons.domain.responses;

public class VoteResult {
    private final String countryCode;
    private final int votes;

    public VoteResult(String countryCode, int votes) {
        this.countryCode = countryCode;
        this.votes = votes;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getVotes() {
        return votes;
    }
}
