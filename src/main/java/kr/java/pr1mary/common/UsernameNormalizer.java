package kr.java.pr1mary.common;

public class UsernameNormalizer {
    public static String normalize(String username) {
        return username == null ? null : username.trim().toLowerCase();
    }
}
