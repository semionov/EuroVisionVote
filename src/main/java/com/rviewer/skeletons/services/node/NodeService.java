package com.rviewer.skeletons.services.node;

import com.rviewer.skeletons.domain.node.Node;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class NodeService {

    private final Set<Node> nodes = new HashSet<>();

    public boolean registerNode(Node node) {
        return nodes.add(node);
    }

    public Set<Node> getAllNodes() {
        return Set.copyOf(nodes);
    }
}
