package com.rviewer.skeletons.services.voting;

public interface VoteService {

    String submitVote(String origin, String destination);

    boolean hasVoted(String origin);
}
