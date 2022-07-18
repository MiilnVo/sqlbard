package com.miilnvo.sqlbard.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MiilnVo
 * @since 2022-07-15
 */
public class TransformMysqlToH2Util {

    public static void main(String[] args) {
        String content = "CREATE TABLE `user` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                "  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                "  `age` int(11) DEFAULT NULL,\n" +
                "  `birthday` date DEFAULT NULL,\n" +
                "  `registerTime` timestamp NULL DEFAULT NULL,\n" +
                "  `enabled` tinyint(1) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";

        content = "SET MODE MYSQL;\n\n" + content;

        content = content.replaceAll("`", "");
        content = content.replaceAll("COLLATE.*(?=D)", "");
        content = content.replaceAll("COMMENT.*'(?=,)", "");
        content = content.replaceAll("\\).*ENGINE.*(?=;)", ")");
        content = content.replaceAll("DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", " AS CURRENT_TIMESTAMP");

        content = uniqueKey(content);

        System.out.println(content);
    }

    private static String uniqueKey(String content) {
        int inc = 0;
        Pattern pattern = Pattern.compile("(?<=KEY )(.*?)(?= \\()");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group() + inc++);
        }
        matcher.appendTail(sb);
        content = sb.toString();
        return content;
    }

}
