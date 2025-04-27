package com.rviewer.skeletons.domain.blockchain;

import java.util.Objects;

public class Vote {

    private final Country origin;
    private final Country destination;
    private long timestamp;

    public Vote(Country origin, Country destination, long timestamp) {
        this.origin = origin;
        this.destination = destination;
        this.timestamp = timestamp;
    }

    public Country getOrigin() {
        return origin;
    }

    public Country getDestination() {
        return destination;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return origin + " -> " + destination;
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
        return origin.equals(vote.origin) && destination.equals(vote.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination);
    }
}
