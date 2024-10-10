package dev.andriipyroh.simpleblockchain.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Helper class for applying operations with SHA-256 hashing algorithm.
 */
public class Sha256Utils {

    /**
     * Applies double SHA-256 hashing algorithm to the input string.
     *
     * @param input the input string to apply the hashing algorithm to
     * @return the resulting hash
     */
    public static String applyDoubleShaHashing256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            hash = digest.digest(hash);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
