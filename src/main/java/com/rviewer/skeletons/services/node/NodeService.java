package com.rviewer.skeletons.services.node;

import com.rviewer.skeletons.domain.node.Node;
import com.rviewer.skeletons.services.p2p.P2PWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class NodeService {

    private final Set<Node> nodes = new HashSet<>();
    private final P2PWebSocketClient webSocketClient;
    private static final Logger logger = LoggerFactory.getLogger(NodeService.class);

    @Autowired
    public NodeService(P2PWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public boolean registerNode(Node node) {
        boolean added = nodes.add(node);
        if (added) {
            connectToPeer(node);
        }
        return added;
    }

    public Set<Node> getAllNodes() {
        return Set.copyOf(nodes);
    }

    private void connectToPeer(Node node) {
        String uri = "ws://" + node.getIp() + ":" + node.getPort() + "/ws";
        try {
            webSocketClient.connectToPeer(uri);
            logger.info("Connected to peer node at " + uri);
        } catch (Exception e) {
            logger.info("Error connecting to peer node: " + e.getMessage());
        }
    }
}
