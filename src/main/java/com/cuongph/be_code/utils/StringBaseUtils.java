package com.cuongph.be_code.utils;

import java.util.UUID;

public class StringBaseUtils {
    public static boolean isNullOrEmpty(String str) {
        return ((str == null) || (str.trim().length() == 0));
    }

    public static String escapeSql(String input) {
        String result = input.trim().replace("/", "//").replace("_", "/_").replace("%", "/%");
        return result;
    }

    public static String sqlStringSearch(String str, boolean isLike) {
        if (isLike)
            return "%" + StringBaseUtils.escapeSql(str.toLowerCase().trim()) + "%";
        else
            return StringBaseUtils.escapeSql(str.toLowerCase().trim());
    }

    public static String genRegisCode() {
        return "HS_" + UUID.randomUUID();
    }
}
