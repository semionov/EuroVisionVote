package com.rviewer.skeletons.infrastructure.controllers;

import com.rviewer.skeletons.domain.node.Node;
import com.rviewer.skeletons.services.node.NodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/p2p/nodes")
public class NodeController {

    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @PostMapping
    public ResponseEntity<String> registerNode(@RequestBody Map<String, Object> request) {
        String ip = (String) request.get("ip");
        Integer port = (Integer) request.get("port");

        if (ip == null || port == null) {
            return ResponseEntity.badRequest().body("Invalid payload");
        }

        boolean added = nodeService.registerNode(new Node(ip, port));
        return added
                ? ResponseEntity.ok("Node registered: ws://" + ip + ":" + port)
                : ResponseEntity.ok("Node already registered");
    }
}
