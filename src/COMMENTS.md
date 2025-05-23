
Architectural solutions:

Event-Based Broadcasting

To decouple core blockchain logic from the networking layer, Spring's event-driven functionality is adopted.  
When a new block is added to the chain, a `NewBlockEvent` is published. The `P2PWebSocketClient` listens for this event and broadcasts the block to all connected peers.

This offers:
- Loose coupling between the blockchain logic and WebSocket code
- Testability of the `BlockchainServiceImpl` 
- Flexibility to extend broadcasting logic later 

---

Blockchain Vote Validation via Chain Scan

Instead of maintaining a separate in-memory structure to track which countries voted, scanning the blockchain itself is applied:
- Before accepting a vote, the chain is scanned to ensure the `originCountryCode` hasn’t already voted.
- This guarantees that no matter which node processed the vote, it is recorded immutably and used for validation.

This offers:
- Immutability-based trust: no duplicate votes
- Validation is based on blockchain content, not RAM

---

P2P Syncing Logic & Safety

Each node establishes WebSocket connections with its peers via `/p2p/nodes`. Once connected:
- When a block is created, it is broadcasts to all peers
- When a node recieves a block, it tries to append it if valid 
- On startup, the full blockchain can be exchanged 

Because the system assumes:
- One update at a time (no concurrent block mining)
- Nodes are trusted (no malicious actors)

---

Assumptions

- Nodes are trusted (no fake peers)
- No mining mechanism: blocks are added directly upon vote
- Consensus is simplified to “longest valid chain wins”
- Each country can vote once

---


Initially the whole chain was broadcasted to ensure state consistency during peer joins. Broadcasting opotimized by sending only the newly added block.

The combination of a clean blockchain model, WebSocket P2P layer, and Spring Boot features like events and REST APIs makes the system both robust and extensible.

