package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.domain.exceptions.DuplicateVoteException;
import com.rviewer.skeletons.domain.responses.VoteResult;
import com.rviewer.skeletons.services.voting.VoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;


import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(VoteController.class)
class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    VoteService voteService;

    @Test
    void submitVote_shouldReturn204() throws Exception {
        mockMvc.perform(post("/votes/FR/IT"))
                .andExpect(status().isNoContent());
    }

    @Test
    void submitVote_shouldReturn409() throws Exception {
        doThrow(new DuplicateVoteException("FR"))
                .when(voteService).submitVote("FR", "IT");

        mockMvc.perform(post("/votes/FR/IT"))
                .andExpect(status().isConflict());
    }

    @Test
    void getVotes_shouldReturnSortedResults() throws Exception {
        when(voteService.getVoteResults()).thenReturn(List.of(
                new VoteResult("IT", 5),
                new VoteResult("FR", 3)
        ));

        mockMvc.perform(get("/votes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].countryCode").value("IT"))
                .andExpect(jsonPath("$[0].votes").value(5));
    }
}
