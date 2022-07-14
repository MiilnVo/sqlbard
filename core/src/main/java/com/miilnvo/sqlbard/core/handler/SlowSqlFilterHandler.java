package com.miilnvo.sqlbard.core.handler;

/**
 * 慢SQL拦截处理
 *
 * @author MiilnVo
 * @since 2022-07-01
 */
public class SlowSqlFilterHandler {

    private Long maxSqlExecuteMillisecond = 1000L;

    private Boolean enabled = false;

    public SlowSqlFilterHandler() {
    }

    public SlowSqlFilterHandler(Long maxSqlExecuteMillisecond, Boolean enabled) {
        this.maxSqlExecuteMillisecond = maxSqlExecuteMillisecond;
        this.enabled = enabled;
    }

    public boolean useSlowSqlFilterAndNotSlowSql(long currentTime) {
        return enabled && currentTime > maxSqlExecuteMillisecond;
    }

}
