package com.miilnvo.sqlbard.core.handler;

import org.apache.ibatis.plugin.Invocation;

import java.util.Properties;

/**
 * @author MiilnVo
 * @since 2022-06-30
 */
public interface SqlBardHandler {

    /**
     * 执行处理
     *
     * @param invocation invocation
     * @param time       time
     * @throws Exception exception
     */
    void execute(Invocation invocation, Long time) throws Exception;

    /**
     * 设置属性
     *
     * @param properties properties
     */
    void setProperties(Properties properties);

}
