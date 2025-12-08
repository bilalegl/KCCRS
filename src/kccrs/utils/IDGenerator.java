package kccrs.utils;

import java.util.UUID;

public class IDGenerator {
    public static String generateID() {
        return "CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
