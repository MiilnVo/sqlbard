package com.miilnvo.sqlbard.core.handler;

import com.miilnvo.sqlbard.core.pojo.SqlBardDefaultParameter;
import com.miilnvo.sqlbard.core.util.SimpleStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author MiilnVo
 * @since 2022-06-30
 */
@Slf4j
public class DefaultSqlBardHandler implements SqlBardHandler {

    private boolean enabled = true;

    private PathFilterHandler pathFilterHandler;

    private SlowSqlFilterHandler slowSqlFilterHandler;

    @Override
    public void execute(Invocation invocation, Long time) throws Exception {
        if (isEnabled() && useSlowSqlFilterAndNotSlowSql(time)) {
            return;
        }
        SqlBardDefaultParameter sqlBardDefaultParameter = buildDefaultParameterHandler(invocation);

        String currentPath = sqlBardDefaultParameter.getMappedStatement().getId();
        if (!isAllowCurrentPath(currentPath)) {
            return;
        }

        String sql = sqlBardDefaultParameter.getBoundSql().getSql();
        sql = SimpleStringUtil.removeWhitespace(sql);

        byte[][] sqlArray = parseSql(sql);

        List<Object> sqlParamList = buildSqlParamList(sqlBardDefaultParameter);

        String completeSql = buildCompleteSql(sqlArray, sqlParamList);

        printSqlLog(completeSql, time);
    }

    @Override
    public void setProperties(Properties properties) {
        enabled = Boolean.valueOf(properties.getProperty("enabled"));
        pathFilterHandler = new PathFilterHandler(
                SimpleStringUtil.convertStrToList(properties.getProperty("allowPathRegexList")),
                SimpleStringUtil.convertStrToList(properties.getProperty("notAllowPathRegexList")));
        slowSqlFilterHandler = new SlowSqlFilterHandler(
                Long.valueOf(properties.getProperty("maxSqlExecuteMillisecond")),
                Boolean.valueOf(properties.getProperty("useSlowSqlFilter")));
    }

    private boolean isEnabled() {
        return enabled;
    }

    private boolean isAllowCurrentPath(String currentPath) {
        return pathFilterHandler.isAllowCurrentPath(currentPath);
    }

    private boolean useSlowSqlFilterAndNotSlowSql(Long time) {
        return slowSqlFilterHandler.useSlowSqlFilterAndNotSlowSql(time);
    }

    /**
     * 构造DefaultParameterHandler
     *
     * @param invocation invocation
     * @return SqlBardDefaultParameter
     * @throws Exception exception
     */
    private SqlBardDefaultParameter buildDefaultParameterHandler(Invocation invocation) throws Exception {

        DefaultParameterHandler defaultParameterHandler = (DefaultParameterHandler) invocation.getTarget();
        Class clazz = defaultParameterHandler.getClass();

        Field parameterObjectField = clazz.getDeclaredField("parameterObject");
        parameterObjectField.setAccessible(true);
        Object parameterObject = parameterObjectField.get(defaultParameterHandler);

        Field mappedStatementField = clazz.getDeclaredField("mappedStatement");
        mappedStatementField.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) mappedStatementField.get(defaultParameterHandler);

        return new SqlBardDefaultParameter(mappedStatement, parameterObject);
    }

    /**
     * 构造SQL变量集合
     * Copy From org.apache.ibatis.scripting.defaults.DefaultParameterHandler
     *
     * @param sqlBardDefaultParameter SqlBardDefaultParameter
     * @return List
     */
    private List<Object> buildSqlParamList(SqlBardDefaultParameter sqlBardDefaultParameter) {

        BoundSql boundSql = sqlBardDefaultParameter.getBoundSql();
        Configuration configuration = sqlBardDefaultParameter.getConfiguration();
        Object parameterObject = sqlBardDefaultParameter.getParameterObject();
        TypeHandlerRegistry typeHandlerRegistry = sqlBardDefaultParameter.getTypeHandlerRegistry();

        List<Object> sqlParamList = new ArrayList<>();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    sqlParamList.add(value);
                }
            }
        }

        return sqlParamList;
    }

    /**
     * 解析SQL
     * Copy From com.mysql.cj.ParseInfo
     *
     * @param sql sql
     * @return byte[][]
     */
    private byte[][] parseSql(String sql) {
        List<int[]> endpointList = new ArrayList<>();
        int lastParamEnd = 0;

        char quoteChar = 0;  // 记录当前的引用符号
        boolean inQuoteChar = false;  // 是否在引用符号之间

        char quotedIdentifierChar = '`';  // 默认的反引符号, 暂不支持SQL_MODE = ansi_quotes时的符号
        boolean inQuotedIdentifierChar = false;  // 是否在反引符号之间

        int sqlLength = sql.length();

        for (int i = 0; i < sqlLength; i++) {
            char c = sql.charAt(i);

            // 忽略转义符
            if (c == '\\' && i < (sqlLength - 1)) {
                i++;
                continue;
            }

            if (!inQuoteChar && c == quotedIdentifierChar) {
                inQuotedIdentifierChar = !inQuotedIdentifierChar;

            } else if (!inQuotedIdentifierChar) {

                if (inQuoteChar) {
                    if (isQuoteChar(c) && c == quoteChar) {
                        // 忽略引用符号之间的所有内容
                        if (i < (sqlLength - 1) && sql.charAt(i + 1) == quoteChar) {
                            i++;
                            continue;
                        }

                        inQuoteChar = false;
                        quoteChar = 0;  // 清空字符
                    }

                } else {
                    if (c == '#' || (c == '-' && (i + 1) < sqlLength && sql.charAt(i + 1) == '-')) {
                        // 忽略注释内容(#和--)
                        int endOfStmt = sqlLength - 1;

                        for (; i < endOfStmt; i++) {
                            c = sql.charAt(i);

                            if (c == '\r' || c == '\n') {
                                break;
                            }
                        }
                        continue;

                    } else if (c == '/' && (i + 1) < sqlLength) {
                        // 忽略注释内容(/**/)
                        char cNext = sql.charAt(i + 1);

                        if (cNext == '*') {
                            i += 2;

                            for (int j = i; j < sqlLength; j++) {
                                i++;
                                cNext = sql.charAt(j);

                                if (cNext == '*' && (j + 1) < sqlLength) {
                                    if (sql.charAt(j + 1) == '/') {
                                        i++;

                                        if (i < sqlLength) {
                                            c = sql.charAt(i);
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                    } else if (isQuoteChar(c)) {
                        // 判断是否为引用符号
                        inQuoteChar = true;
                        quoteChar = c;
                    }
                }
            }

            if (!inQuoteChar && !inQuotedIdentifierChar) {
                if (c == '?') {
                    // 记录'?'的下标
                    endpointList.add(new int[]{lastParamEnd, i});
                    lastParamEnd = i + 1;

                } else if (c == ';') {
                    // 忽略';'后的所有空白字符
                    int j = i + 1;
                    if (j < sqlLength) {
                        for (; j < sqlLength; j++) {
                            if (!Character.isWhitespace(sql.charAt(j))) {
                                break;
                            }
                        }
                        i = j - 1;
                    }
                }
            }

        }

        endpointList.add(new int[]{lastParamEnd, sql.length()});

        byte[][] staticSql = new byte[endpointList.size()][];
        for (int i = 0; i < staticSql.length; i++) {
            int[] point = endpointList.get(i);
            int begin = point[0];
            int end = point[1];
            staticSql[i] = sql.substring(begin, end).getBytes(StandardCharsets.UTF_8);
        }

        return staticSql;
    }

    private boolean isQuoteChar(char c) {
        return c == '\'' || c == '"';
    }

    /**
     * 生成完整的SQL
     *
     * @param sqlArray     解析后的SQL数组
     * @param sqlParamList SQL变量集合
     * @return String
     */
    private String buildCompleteSql(byte[][] sqlArray, List<Object> sqlParamList) {
        StringBuilder completeSql = new StringBuilder();
        for (int i = 0; i < sqlParamList.size(); ++i) {
            completeSql.append(new String(sqlArray[i]));
            Object param = sqlParamList.get(i);
            if (param instanceof String) {
                completeSql.append("'");
                completeSql.append(param);
                completeSql.append("'");
            } else {
                completeSql.append(param);
            }
        }
        completeSql.append(new String(sqlArray[sqlParamList.size()]));

        return completeSql.toString();
    }

    /**
     * 输出日志
     *
     * @param sql sql
     */
    private void printSqlLog(String sql, long time) {
        log.info("[SQLBard] sql = {}, time = {}ms", sql, time);
    }

}
