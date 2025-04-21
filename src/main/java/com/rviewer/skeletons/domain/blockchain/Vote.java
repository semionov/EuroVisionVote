package com.rviewer.skeletons.domain.blockchain;

import java.util.Objects;

public class Vote {

    private final String origin;
    private final String destination;

    public Vote(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
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
