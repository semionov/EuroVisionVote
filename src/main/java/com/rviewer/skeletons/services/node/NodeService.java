package com.rviewer.skeletons.services.node;

import com.rviewer.skeletons.domain.node.Node;

import java.util.Set;

public interface NodeService {

    /**
     * Registers a new peer node to the local node registry.
     *
     * @param node The node to register.
     * @return boolean True if the node was newly registered, false if it already exists.
     */
    boolean registerNode(Node node);

    /**
     * Retrieves all registered peer nodes.
     *
     * @return Set<Node> A set of all known nodes in the network.
     */
    Set<Node> getAllNodes();
}
