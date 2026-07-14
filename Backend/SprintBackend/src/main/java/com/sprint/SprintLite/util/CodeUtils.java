package com.sprint.SprintLite.util;

public class CodeUtils {

 public static String encode(String prefix, Integer id) {
        if (id == null) {
            return null;
        }
        return prefix + id;
    }

    public static String encode(String prefix, Long id) {
        if (id == null) {
            return null;
        }
        return prefix + id;
    }

    public static Integer decodeToInteger(String prefix, String code) {
        if (code == null || code.isEmpty() || !code.startsWith(prefix)) {
            return null;
        }
        try {
            return Integer.parseInt(code.substring(prefix.length()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long decodeToLong(String prefix, String code) {
        if (code == null || code.isEmpty() || !code.startsWith(prefix)) {
            return null;
        }
        try {
            return Long.parseLong(code.substring(prefix.length()));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}