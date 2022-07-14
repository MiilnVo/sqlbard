package com.miilnvo.sqlbard.core.interceptor;

import com.miilnvo.sqlbard.core.common.SystemClock;
import com.miilnvo.sqlbard.core.handler.DefaultSqlBardHandler;
import com.miilnvo.sqlbard.core.handler.SqlBardHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;

import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * SQLBard核心拦截器
 *
 * @author MiilnVo
 * @since 2022-06-28
 */
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class})
})
public class SqlBardInterceptor implements Interceptor {

    private SqlBardHandler sqlBardHandler = new DefaultSqlBardHandler();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (sqlBardHandler.isShowExecuteTime()) {
            long time = -1;
            try {
                long startTime = SystemClock.now();
                Object result = invocation.proceed();
                time = SystemClock.now() - startTime;
                return result;
            } finally {
                sqlBardHandler.execute(invocation, time);
            }
        } else {
            try {
                return invocation.proceed();
            } finally {
                sqlBardHandler.execute(invocation);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ParameterHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        sqlBardHandler.setProperties(properties);
    }

}
