package com.rviewer.skeletons.domain.blockchain;

import java.util.Objects;

public class Vote {

    private final Country originCountryCode;
    private final Country destinationCountryCode;
    private final long timestamp;

    public Vote() {
        this.originCountryCode = null;
        this.destinationCountryCode = null;
        this.timestamp = 0L;
    }

    public Vote(Country originCountryCode, Country destinationCountryCode, long timestamp) {
        this.originCountryCode = originCountryCode;
        this.destinationCountryCode = destinationCountryCode;
        this.timestamp = timestamp;
    }


    public Country getOriginCountryCode() {
        return originCountryCode;
    }

    public Country getDestinationCountryCode() {
        return destinationCountryCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return originCountryCode + " -> " + destinationCountryCode;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Vote)){
            return false;
        }

        Vote vote = (Vote) o;
        return originCountryCode.equals(vote.originCountryCode) && destinationCountryCode.equals(vote.destinationCountryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originCountryCode, destinationCountryCode);
    }
}
