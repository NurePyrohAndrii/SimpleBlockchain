import dev.andriipyroh.simpleblockchain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static dev.andriipyroh.Client.getBlockchain;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BlockchainTest {

    Blockchain blockchain;

    @BeforeEach
    void setUp() {
        this.blockchain = getBlockchain();
    }

    @Test
    void testAddBlockAndCheckValidity() {
        blockchain.addBlock(getTransactions(500));
        blockchain.addBlock(getTransactions(1000));
        assertDoesNotThrow(blockchain::checkBlockchain);
    }

    @Test
    void testAddBlockWithInvalidTransactions() {
        assertThrows(IllegalArgumentException.class, () -> blockchain.addBlock(getTransactions(10001)));
    }

    @Test
    void checkBlockchainIntegrityAfterHashTampering() throws NoSuchFieldException, IllegalAccessException {
        blockchain.addBlock(getTransactions(500));
        blockchain.addBlock(getTransactions(501));
        blockchain.addBlock(getTransactions(502));

        Field blockChain = Blockchain.class.getDeclaredField("blockchain");
        blockChain.setAccessible(true);
        LinkedList<Block> blocks = (LinkedList<Block>) blockChain.get(blockchain);
        Field hash = Hashable.class.getDeclaredField("hash");
        hash.setAccessible(true);
        hash.set(blocks.get(1), "1".repeat(64));

        assertThrows(IllegalStateException.class, blockchain::checkBlockchain);
    }

    @Test
    void checkBlockchainIntegrityAfterTransactionTampering() throws NoSuchFieldException, IllegalAccessException {
        blockchain.addBlock(getTransactions(500));
        blockchain.addBlock(getTransactions(501));
        blockchain.addBlock(getTransactions(502));

        Field blockChain = Blockchain.class.getDeclaredField("blockchain");
        blockChain.setAccessible(true);
        LinkedList<Block> blocks = (LinkedList<Block>) blockChain.get(blockchain);
        Field transactions = Block.class.getDeclaredField("transactions");
        transactions.setAccessible(true);
        transactions.set(blocks.get(1), getTransactions(503));
    }

    private List<Transaction> getTransactions(int amount) {
        return List.of(
                new Transaction(User.MIKE, User.ALICE, amount),
                new Transaction(User.ALICE, User.CHARLIE, amount),
                new Transaction(User.CHARLIE, User.MIKE, amount),
                new Transaction(User.MIKE, User.ALICE, amount),
                new Transaction(User.ALICE, User.CHARLIE, amount),
                new Transaction(User.CHARLIE, User.MIKE, amount)
        );
    }
}
