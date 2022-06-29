package com.miilnvo.sqlbard.starter.config;

import com.miilnvo.sqlbard.core.util.SimpleStringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Properties;

/**
 * @author MiilnVo
 * @since 2022-06-28
 */
@ConfigurationProperties(prefix = SqlBardProperties.SQLBARD_PREFIX)
public class SqlBardProperties extends Properties {

    /** 指定前缀 */
    static final String SQLBARD_PREFIX = "sqlbard";

    public Boolean isEnabled() {
        return Boolean.valueOf(getProperty("enabled") != null ? getProperty("enabled") : "true");
    }

    public void setEnabled(Boolean enabled) {
        setProperty("enabled", enabled.toString());
    }

    public List<String> getAllowPathRegexList() {
        return SimpleStringUtil.convertStrToList(getProperty("allowPathRegexList"));
    }

    public void setAllowPathRegexList(List<String> allowPathRegexList) {
        setProperty("allowPathRegexList", SimpleStringUtil.convertListToStr(allowPathRegexList));
    }

    public List<String> getNotAllowPathRegexList() {
        return SimpleStringUtil.convertStrToList(getProperty("getNotAllowPathRegexList"));
    }

    public void setNotAllowPathRegexList(List<String> notAllowPathRegexList) {
        setProperty("notAllowPathRegexList", SimpleStringUtil.convertListToStr(notAllowPathRegexList));
    }

    public Boolean isUseSlowSqlFilter() {
        return Boolean.valueOf(getProperty("isUseSlowSqlFilter") != null ? getProperty("isUseSlowSqlFilter") : "false");
    }

    public void setUseSlowSqlFilter(Boolean useSlowSqlFilter) {
        setProperty("useSlowSqlFilter", useSlowSqlFilter.toString());
    }

    public Boolean isShowSqlExecuteTime() {
        return Boolean.valueOf(getProperty("isShowSqlExecuteTime") != null ? getProperty("isShowSqlExecuteTime") : "true");
    }

    public void setShowSqlExecuteTime(Boolean showSqlExecuteTime) {
        setProperty("showSqlExecuteTime", showSqlExecuteTime.toString());
    }

    public Long getMaxSqlExecuteMillisecond() {
        return Long.valueOf(getProperty("getMaxSqlExecuteMillisecond"));
    }

    public void setMaxSqlExecuteMillisecond(Long maxSqlExecuteMillisecond) {
        setProperty("maxSqlExecuteMillisecond", maxSqlExecuteMillisecond.toString());
    }
}
