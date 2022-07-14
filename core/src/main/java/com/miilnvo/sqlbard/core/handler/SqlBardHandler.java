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
     * @throws Exception exception
     */
    void execute(Invocation invocation) throws Exception;

    /**
     * 执行处理
     *
     * @param invocation  invocation
     * @param executeTime executeTime
     * @throws Exception exception
     */
    void execute(Invocation invocation, Long executeTime) throws Exception;

    /**
     * 设置属性
     *
     * @param properties properties
     */
    void setProperties(Properties properties);

    /**
     * 是否显示执行时间
     *
     * @return Boolean
     */
    Boolean isShowExecuteTime();

}
