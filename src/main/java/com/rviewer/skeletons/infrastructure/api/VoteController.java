package com.rviewer.skeletons.infrastructure.api;

import com.rviewer.skeletons.domain.exceptions.DuplicateVoteException;
import com.rviewer.skeletons.domain.exceptions.InvalidCountryException;
import com.rviewer.skeletons.domain.responses.VoteResult;
import com.rviewer.skeletons.services.voting.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/{originCountryCode}/{destinationCountryCode}")
    public ResponseEntity<?> submitVote(
            @PathVariable String originCountryCode,
            @PathVariable String destinationCountryCode) {

        try {
            voteService.submitVote(originCountryCode, destinationCountryCode);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (DuplicateVoteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage())); // HTTP 409
        } catch (InvalidCountryException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage())); // HTTP 404
        }
    }

    @GetMapping
    public ResponseEntity<List<VoteResult>> getVotes() {
        return ResponseEntity.ok(voteService.getVoteResults());
    }
}
