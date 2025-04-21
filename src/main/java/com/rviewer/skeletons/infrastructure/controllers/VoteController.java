package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.services.voting.VoteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/{origin}/{destination}")
    public String submitVote(@PathVariable String origin, @PathVariable String destination) {
        // Invoke voteService and submit a vote
        return voteService.submitVote(origin, destination);
    }
}
