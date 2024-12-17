package ru.itmo.serverlessorback.utils;

import ru.itmo.is_lab1.exceptions.util.CanNotCreateHashException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class SHA512HashUtil {
    public static String hash(String input) throws CanNotCreateHashException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes); // Converts to a hex string
        } catch (NoSuchAlgorithmException e) {
            throw new CanNotCreateHashException("SHA-512 algorithm not available");
        }
    }
}
