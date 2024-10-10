package dev.andriipyroh.simpleblockchain;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a block in the blockchain.
 *
 * <p>Each block contains a list of transactions, a reference to the previous block, a timestamp, and a hash.
 * The hash is calculated based on the previous hash, the transactions, and the timestamp.
 * The block is mined by finding a hash that starts with a certain number of zeros (difficulty).
 */
@Getter
@ToString
public class Block extends Hashable {

    private final String previousHash;
    private final List<Transaction> transactions;
    private final Instant timestamp;
    private int nonce;

    public Block(List<Transaction> transactions, String previousHash) {
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.timestamp = Instant.now();
        this.hash = calculateHash();
    }

    /**
     * Mines the block by finding a hash that starts with a certain number of zeros (difficulty).
     *
     * @param difficulty the number of zeros the hash should start with
     */
    public void mine(ZerosHashConsensus zerosHashConsensus) {
        while (!zerosHashConsensus.isHashValid(this)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    /**
     * Returns a map representing the changes in account balances for transactions in the block.
     *
     * @return a map of account holders and their respective balance changes from the transactions in the block
     */
    public Map<User, Double> getFundsMovement() {
        return transactions.stream()
                .flatMap(transaction -> transaction.getFundsMovement().entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingDouble(Map.Entry::getValue)
                ));
    }

    @Override
    protected String getHashInput() {
        return toString();
    }
}