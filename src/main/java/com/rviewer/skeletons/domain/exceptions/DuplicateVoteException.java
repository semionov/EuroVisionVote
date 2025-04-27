package com.rviewer.skeletons.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateVoteException extends RuntimeException {
    public DuplicateVoteException(String countryCode) {
        super(String.format("Country %s has already voted", countryCode));
    }
}
