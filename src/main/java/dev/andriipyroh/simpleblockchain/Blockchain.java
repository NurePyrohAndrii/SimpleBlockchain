package dev.andriipyroh.simpleblockchain;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.andriipyroh.simpleblockchain.util.BlockUtils.isHashesEqual;

/**
 * Blockchain class is a list of blocks.
 */
public class Blockchain {

    private final LinkedList<Block> blockchain;

    private final ZerosHashConsensus zerosHashConsensus;

    public Blockchain(ZerosHashConsensus zerosHashConsensus, List<Transaction> transactions) {
        this.zerosHashConsensus = zerosHashConsensus;
        this.blockchain = new LinkedList<>(List.of(new Block(transactions, "0".repeat(zerosHashConsensus.getDifficulty()))));
    }

    /**
     * Adds a block to the blockchain with the given list of transactions. The new block is mined till the hash fulfills the consensus algorithm.
     *
     * @param transactions list of transactions to add to the block
     */
    public void addBlock(List<Transaction> transactions) {
        checkNewTransactions(transactions);
        Block newBlock = new Block(transactions, getLastBlock().getHash());
        if (!zerosHashConsensus.isHashValid(newBlock)) {
            newBlock.mine(zerosHashConsensus);
        }
        blockchain.add(newBlock);
        System.out.printf("Block added: %s,%n" +
                "Notifying all nodes about the new block...", newBlock);
    }

    /**
     * Checks if the blockchain is valid. The blockchain is valid if all blocks have the correct previous hash, and the hash of the block fulfills the consensus algorithm.
     * Also, the hash of the block should be equal to the calculated hash of the block to ensure the integrity of the block.
     */
    public void checkBlockchain() {
        ListIterator<Block> iterator = blockchain.listIterator(blockchain.size() - 1);
        Block currentBlock = iterator.previous();
        Block previousBlock = iterator.previous();
        while (iterator.hasPrevious()) {
            if (!isHashesEqual(currentBlock.getPreviousHash(), previousBlock.getHash()) ||
                    !zerosHashConsensus.isHashValid(currentBlock) ||
                    !currentBlock.getHash().equals(currentBlock.calculateHash())
            ) {
                throw new IllegalStateException("Blockchain is not valid");
            }
            currentBlock = previousBlock;
            previousBlock = iterator.previous();
        }
    }

    /**
     * Returns the balances of all account holders in the blockchain.
     *
     * @return a map of account holders and their respective balances
     */
    public Map<User, Double> getBalances() {
        return blockchain.stream()
                .flatMap(block -> block.getFundsMovement().entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingDouble(Map.Entry::getValue)
                ));
    }

    /**
     * Checks if the new transactions are valid. The transactions are valid if the sender has enough balance to send the amount.
     *
     * @param transactions list of transactions to check
     */
    private void checkNewTransactions(List<Transaction> transactions) {
        Map<User, Double> balances = getBalances();
        transactions.forEach(transaction -> {
            if (balances.getOrDefault(transaction.getSender(), 0.0) < transaction.getAmount()) {
                throw new IllegalArgumentException("Transaction is not valid: " + transaction);
            }
        });
    }

    /**
     * Returns the last block in the blockchain.
     *
     * @return the last block in the blockchain
     */
    private Block getLastBlock() {
        return blockchain.getLast();
    }

}
