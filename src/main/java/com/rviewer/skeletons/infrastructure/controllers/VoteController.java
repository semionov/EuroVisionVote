package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.domain.exceptions.DuplicateVoteException;
import com.rviewer.skeletons.domain.exceptions.InvalidCountryException;
import com.rviewer.skeletons.domain.responses.VoteResult;
import com.rviewer.skeletons.services.voting.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/{originCountryCode}/{destinationCountryCode}")
    public ResponseEntity<Void> submitVote(
            @PathVariable String originCountryCode,
            @PathVariable String destinationCountryCode) {

        try {
            voteService.submitVote(originCountryCode, destinationCountryCode);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (DuplicateVoteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // HTTP 409
        } catch (InvalidCountryException e) {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }

    @GetMapping
    public ResponseEntity<List<VoteResult>> getVotes() {
        return ResponseEntity.ok(voteService.getVoteResults());
    }
}
