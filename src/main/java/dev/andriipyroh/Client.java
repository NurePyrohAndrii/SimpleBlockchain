package dev.andriipyroh;

import dev.andriipyroh.simpleblockchain.Blockchain;
import dev.andriipyroh.simpleblockchain.Transaction;
import dev.andriipyroh.simpleblockchain.User;
import dev.andriipyroh.simpleblockchain.ZerosHashConsensus;

import java.util.List;

public class Client {

    public static void main(String[] args) {
        try {
            // initializing of the blockchain
            Blockchain blockchain = getBlockchain();

            // adding of a block to the blockchain
            List<Transaction> transactions = List.of(
                    new Transaction(User.MIKE, User.ALICE, 500)
            );
            blockchain.addBlock(transactions);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public static Blockchain getBlockchain() {
        ZerosHashConsensus zerosHashConsensus = new ZerosHashConsensus(4);
        List<Transaction> genesisTransactions = List.of(
                new Transaction(User.SYSTEM, User.MIKE, 10000),
                new Transaction(User.SYSTEM, User.ALICE, 10000),
                new Transaction(User.SYSTEM, User.CHARLIE, 10000)
        );
        return new Blockchain(zerosHashConsensus, genesisTransactions);
    }
}