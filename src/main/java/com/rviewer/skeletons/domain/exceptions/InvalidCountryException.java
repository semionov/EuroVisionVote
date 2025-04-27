package com.rviewer.skeletons.domain.exceptions;

import com.rviewer.skeletons.domain.blockchain.Country;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCountryException extends RuntimeException {

    private final String countryCode;

    public InvalidCountryException(String countryCode) {
        super(String.format("Invalid country code: %s. Valid codes are: %s",
                countryCode, Country.validCodes()));
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
