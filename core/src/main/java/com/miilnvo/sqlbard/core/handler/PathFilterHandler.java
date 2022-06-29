package com.miilnvo.sqlbard.core.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * 指定路径拦截处理
 *
 * @author MiilnVo
 * @since 2022-07-01
 */
public class PathFilterHandler {

    private List<String> allowPathRegexList = new ArrayList<>();

    private List<String> notAllowPathRegexList = new ArrayList<>();

    public PathFilterHandler() {
    }

    public PathFilterHandler(List<String> allowPathRegexList, List<String> notAllowPathRegexList) {
        this.allowPathRegexList = allowPathRegexList;
        this.notAllowPathRegexList = notAllowPathRegexList;
    }

    public boolean isAllowCurrentPath(String currentPath) {
        boolean isAllow = true;
        if (allowPathRegexList != null && allowPathRegexList.size() > 0) {
            for (String allowPathRegex : allowPathRegexList) {
                isAllow &= currentPath.matches(allowPathRegex);
            }
        }
        if (notAllowPathRegexList != null && notAllowPathRegexList.size() > 0) {
            for (String notAllowPathRegex : notAllowPathRegexList) {
                isAllow &= !currentPath.matches(notAllowPathRegex);
            }
        }
        return isAllow;
    }

}
