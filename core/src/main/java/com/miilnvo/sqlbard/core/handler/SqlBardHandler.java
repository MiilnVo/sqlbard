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
     */
    void execute(Invocation invocation);

    /**
     * 执行处理
     *
     * @param invocation  invocation
     * @param executeTime executeTime
     */
    void execute(Invocation invocation, Long executeTime);

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
