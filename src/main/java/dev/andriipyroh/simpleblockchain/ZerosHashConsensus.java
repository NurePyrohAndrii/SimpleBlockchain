package dev.andriipyroh.simpleblockchain;

import lombok.Getter;

/**
 * HashConsensus represents a consensus algorithm that requires a hash to have a certain number of leading zeros.
 */
@Getter
public class ZerosHashConsensus {

    /**
     * The Difficulty is the number of leading zeros required in the hash.
     */
    private final int difficulty;

    /**
     * The targetHashPrefix is a string of zeros with the length of the difficulty.
     */
    private final String targetHashPrefix;

    public ZerosHashConsensus(int difficulty) {
        if (difficulty < 1) {
            throw new IllegalArgumentException("Difficulty must be greater than 0");
        }
        this.difficulty = difficulty;
        this.targetHashPrefix = "0".repeat(difficulty);
    }

    /**
     * Checks if the hash has the required prefix fulfilling the consensus algorithm.
     * @param hash hash to check
     * @return true if the hash has the required number of leading zeros, false otherwise
     */
    public boolean isHashValid(Hashable hashable) {
        return hashable.getHash().startsWith(targetHashPrefix);
    }

}
