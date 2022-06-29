package com.miilnvo.sqlbard.core.handler;

/**
 * 慢SQL拦截处理
 *
 * @author MiilnVo
 * @since 2022-07-01
 */
public class SlowSqlFilterHandler {

    private long maxSqlExecuteMillisecond = 1000;

    private boolean enabled = false;

    public SlowSqlFilterHandler() {
    }

    public SlowSqlFilterHandler(long maxSqlExecuteMillisecond, boolean enabled) {
        this.maxSqlExecuteMillisecond = maxSqlExecuteMillisecond;
        this.enabled = enabled;
    }

    public boolean useSlowSqlFilterAndNotSlowSql(long currentTime) {
        return enabled && currentTime > maxSqlExecuteMillisecond;
    }

}
