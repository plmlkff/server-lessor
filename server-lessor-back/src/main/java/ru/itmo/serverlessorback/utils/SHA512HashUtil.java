package ru.itmo.serverlessorback.utils;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class SHA512HashUtil implements HashUtil {
    @Override
    public String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 алгоритм не поддерживается");
        }
    }

    @Override
    public boolean check(String password, String hash) {
        return hash(password).equals(hash);
    }
}
