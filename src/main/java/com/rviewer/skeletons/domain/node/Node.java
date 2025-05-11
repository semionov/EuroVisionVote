package com.rviewer.skeletons.domain.node;

public class Node {
    private final String ip;
    private final int port;

    public Node(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getAddress() {
        return "ws://" + ip + ":" + port;
    }

    // equals and hashCode to prevent duplicate nodes in a Set
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Node)) {
            return false;
        }

        Node other = (Node) o;
        return this.getAddress().equals(other.getAddress());
    }

    @Override
    public int hashCode() {
        return getAddress().hashCode();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
