package dev.andriipyroh.simpleblockchain;

import dev.andriipyroh.Client;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a transaction in the blockchain.
 *
 * <p>Each transaction contains the sender, the recipient, the amount, the timestamp, and the hash.
 */
@Getter
@ToString
public class Transaction extends Hashable {

    private final User sender;
    private final User recipient;
    private final double amount;
    private final Instant timestamp;

    public Transaction(User sender, User recipient, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = Instant.now();
        this.hash = calculateHash();
    }

    /**
     * Returns a map representing the changes in account balances for the sender and recipient.
     *
     * @return a map of account holders (sender and recipient) and their respective balance changes
     */
    public Map<User, Double> getFundsMovement() {
        return Map.of(
                sender, -amount,
                recipient, amount
        );
    }

    @Override
    protected String getHashInput() {
        return toString();
    }
}
