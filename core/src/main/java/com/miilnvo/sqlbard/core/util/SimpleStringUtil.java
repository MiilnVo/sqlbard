package com.miilnvo.sqlbard.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * @author MiilnVo
 * @since 2022-06-30
 */
public final class SimpleStringUtil {

    /**
     * 移除字符串中的空白符
     *
     * @param rawStr 原始字符串
     * @return String
     */
    public static String removeWhitespace(String rawStr) {
        StringTokenizer stringTokenizer = new StringTokenizer(rawStr);
        StringBuilder builder = new StringBuilder();
        while (stringTokenizer.hasMoreTokens()) {
            builder.append(stringTokenizer.nextToken());
            builder.append(" ");
        }
        return builder.toString();
    }

    public static List<String> convertStrToList(String rawStr) {
        List<String> result = new ArrayList<>();
        if (Objects.isNull(rawStr) || rawStr.length() == 0) {
            return result;
        }
        String[] split = rawStr.split(",");
        for (String s : split) {
            result.add(s.trim());
        }
        return result;
    }

    public static String convertListToStr(List<String> rawList) {
        if (Objects.isNull(rawList)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : rawList) {
            stringBuilder.append(str);
            stringBuilder.append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

}
