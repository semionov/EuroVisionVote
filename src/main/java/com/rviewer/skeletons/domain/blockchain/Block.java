package com.rviewer.skeletons.domain.blockchain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Block {
    private String hash;
    private String previousHash;
    private long timestamp;
    private Vote vote;

    @JsonIgnore
    public static Block getGenesisBlock() {
        Block genesis = new Block();
        genesis.setTimestamp(0L);
        genesis.setPreviousHash("0");
        genesis.setHash("f1534392279bddbf9d43dde8701cb5be14b82f76ec6607bf8d6ad557f60f304e");
        return genesis;
    }

    public Block() {
    }


    public static String generateHashFromBlock(Block block) {
        String input = block.getTimestamp() + block.getPreviousHash() +
                (block.getVote() != null ? block.getVote().toString() : "");
        return toSha256Hex(input);
    }

    private static String toSha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 unavailable", e);
        }
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }
}
