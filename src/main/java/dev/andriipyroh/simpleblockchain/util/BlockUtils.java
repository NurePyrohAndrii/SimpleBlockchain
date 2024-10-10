package dev.andriipyroh.simpleblockchain.util;

import java.util.Objects;

public class BlockUtils {

    public static boolean isHashesEqual(String hash1, String hash2) {
        return Objects.equals(hash1, hash2);
    }

}