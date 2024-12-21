package ru.itmo.serverlessorback.utils;

public interface HashUtil {
    String hash(String password);
    boolean check(String password, String hash);
}
