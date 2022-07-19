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
        return getProperty("enabled") != null ? Boolean.valueOf(getProperty("enabled")) : true;
    }

    public void setEnabled(Boolean enabled) {
        setProperty("enabled", enabled ? enabled.toString() : "true");
    }

    public List<String> getAllowPathList() {
        return SimpleStringUtil.convertStrToList(getProperty("allowPathList"));
    }

    public void setAllowPathList(List<String> allowPathList) {
        setProperty("allowPathList", SimpleStringUtil.convertListToStr(allowPathList));
    }

    public List<String> getNotAllowPathList() {
        return SimpleStringUtil.convertStrToList(getProperty("notAllowPathList"));
    }

    public void setNotAllowPathList(List<String> notAllowPathList) {
        setProperty("notAllowPathList", SimpleStringUtil.convertListToStr(notAllowPathList));
    }

    public Boolean getShowExecuteTime() {
        return getProperty("showExecuteTime") != null ? Boolean.valueOf(getProperty("showExecuteTime")) : false;
    }

    public void setShowExecuteTime(Boolean showExecuteTime) {
        setProperty("showExecuteTime", showExecuteTime ? showExecuteTime.toString() : "true");
    }

    public Long getMaxExecuteMillisecond() {
        return Long.valueOf(getProperty("maxExecuteMillisecond"));
    }

    public void setMaxExecuteMillisecond(Long maxExecuteMillisecond) {
        setProperty("maxExecuteMillisecond", maxExecuteMillisecond.toString());
    }

    public String getBooleanStrategy() {
        return getProperty("booleanStrategy") != null ? getProperty("booleanStrategy") : "toNumber";
    }

    public void setBooleanStrategy(String booleanStrategy) {
        setProperty("booleanStrategy", booleanStrategy != null ? booleanStrategy : "toNumber");
    }

    public String getEnumStrategy() {
        return getProperty("enumStrategy") != null ? getProperty("enumStrategy") : "toName";
    }

    public void setEnumStrategy(String enumStrategy) {
        setProperty("enumStrategy", enumStrategy != null ? enumStrategy : "toName");
    }

}
