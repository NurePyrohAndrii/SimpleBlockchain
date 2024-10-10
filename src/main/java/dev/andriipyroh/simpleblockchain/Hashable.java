package dev.andriipyroh.simpleblockchain;

import lombok.Getter;

import static dev.andriipyroh.simpleblockchain.util.Sha256Utils.applyDoubleShaHashing256;

/**
 * Represents a hashed object.
 */
@Getter
public abstract class Hashable {

    protected String hash;

    /**
     * Abstract method that subclasses should implement to define what constitutes the hash input.
     *
     * @return a string representation used to calculate the hash
     */
    protected abstract String getHashInput();

    /**
     * Calculates the hash of the entity based on the specific hash input.
     *
     * @return the calculated hash value
     */
    protected String calculateHash() {
        return applyDoubleShaHashing256(getHashInput());
    }
}
