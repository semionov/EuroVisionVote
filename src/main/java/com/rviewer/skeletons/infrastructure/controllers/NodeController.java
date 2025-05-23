package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.domain.node.Node;
import com.rviewer.skeletons.services.node.NodeService;
import com.rviewer.skeletons.services.p2p.P2PWebSocketClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/p2p/nodes")
public class NodeController {

    private final NodeService nodeService;
    private final P2PWebSocketClient p2PWebSocketClient;

    public NodeController(NodeService nodeService, P2PWebSocketClient p2PWebSocketClient) {
        this.nodeService = nodeService;
        this.p2PWebSocketClient = p2PWebSocketClient;
    }

    @PostMapping
    public ResponseEntity<String> registerNode(@RequestBody Map<String, Object> request) throws Exception {
        String ip = (String) request.get("ip");
        Integer port = (Integer) request.get("port");

        if (ip == null || port == null) {
            return ResponseEntity.badRequest().body("Invalid payload");
        }

        boolean added = nodeService.registerNode(new Node(ip, port));

        if (added) {
            return ResponseEntity.ok("Node registered and connected: ws://" + ip + ":" + port);
        } else {
            return ResponseEntity.ok("Node already registered");
        }
    }
}
