package com.miilnvo.sqlbard.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 指定路径拦截处理
 *
 * @author MiilnVo
 * @since 2022-07-01
 */
public class PathFilterHandler {

    private List<String> allowPathList = new ArrayList<>();

    private List<String> notAllowPathList = new ArrayList<>();

    public PathFilterHandler() {
    }

    public PathFilterHandler(List<String> allowPathList, List<String> notAllowPathList) {
        this.allowPathList = allowPathList;
        this.notAllowPathList = notAllowPathList;
    }

    public boolean isAllowCurrentPath(String currentPath) {
        return handleAllowPathList(currentPath) && handleNotAllowPathList(currentPath);
    }

    private boolean handleAllowPathList(String currentPath) {
        boolean allowPathResult = false;
        if (allowPathList == null || allowPathList.size() == 0) {
            allowPathResult = true;
        } else {
            for (String allowPath : allowPathList) {
                int samePath = 0;
                String[] allowPaths = allowPath.split("\\.");
                String[] currentPaths = currentPath.split("\\.");
                for (int i = 0; i < allowPaths.length; i++) {
                    if (Objects.equals(allowPaths[i], "*") || Objects.equals(allowPaths[i], currentPaths[i])) {
                        samePath++;
                    }
                }
                if (samePath == allowPaths.length) {
                    allowPathResult = true;
                    break;
                }
            }
        }
        return allowPathResult;
    }

    private boolean handleNotAllowPathList(String currentPath) {
        boolean notAllowPathResult = true;
        if (notAllowPathList != null && notAllowPathList.size() > 0) {
            for (String notAllowPath : notAllowPathList) {
                int samePath = 0;
                String[] notAllowPaths = notAllowPath.split("\\.");
                String[] currentPaths = currentPath.split("\\.");
                for (int i = 0; i < notAllowPaths.length; i++) {
                    if (Objects.equals(notAllowPaths[i], "*") || Objects.equals(notAllowPaths[i], currentPaths[i])) {
                        samePath++;
                    }
                }
                if (samePath == notAllowPaths.length) {
                    notAllowPathResult = false;
                    break;
                }
            }
        }
        return notAllowPathResult;
    }

}
