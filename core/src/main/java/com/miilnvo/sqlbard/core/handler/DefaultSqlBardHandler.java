package com.miilnvo.sqlbard.core.handler;

import com.miilnvo.sqlbard.core.pojo.SqlBardParameter;
import com.miilnvo.sqlbard.core.util.SimpleStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
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

    private Boolean enabled = true;

    private Boolean showExecuteTime = false;

    private Long maxExecuteMillisecond = -1L;

    private PathFilterHandler pathFilterHandler;

    private TypeConvertHandler typeConvertHandler;

    @Override
    public void execute(Invocation invocation, Long executeTime) {
        try {
            if (!isEnabled()) {
                return;
            }

            SqlBardParameter sqlBardParameter = buildParameterHandler(invocation);

            String currentPath = sqlBardParameter.getMappedStatement().getId();
            if (!isAllowCurrentPath(currentPath)) {
                return;
            }

            String sql = sqlBardParameter.getBoundSql().getSql();
            sql = SimpleStringUtil.removeWhitespace(sql);

            byte[][] sqlArray = parseSql(sql);

            List<Object> sqlParamList = buildSqlParamList(sqlBardParameter);

            String completeSql = buildCompleteSql(sqlArray, sqlParamList);

            printSqlLog(completeSql, executeTime);

        } catch (Exception e) {
            log.error("[SQLBard] execute error: ", e);
        }
    }

    @Override
    public void execute(Invocation invocation) {
        execute(invocation, null);
    }

    @Override
    public void setProperties(Properties properties) {
        String enabled = properties.getProperty("enabled");
        String showExecuteTime = properties.getProperty("showExecuteTime");
        String maxExecuteMillisecond = properties.getProperty("maxExecuteMillisecond");

        if (enabled != null) {
            this.enabled = Boolean.valueOf(enabled);
        }
        if (showExecuteTime != null) {
            this.showExecuteTime = Boolean.valueOf(showExecuteTime);
        }
        if (maxExecuteMillisecond != null) {
            this.maxExecuteMillisecond = Long.valueOf(maxExecuteMillisecond);
        }

        pathFilterHandler = new PathFilterHandler(
                SimpleStringUtil.convertStrToList(properties.getProperty("allowPathList")),
                SimpleStringUtil.convertStrToList(properties.getProperty("notAllowPathList")));

        typeConvertHandler = new TypeConvertHandler(
                properties.getProperty("booleanStrategy"),
                properties.getProperty("enumStrategy"));
    }

    private Boolean isEnabled() {
        return enabled;
    }

    private Long getMaxExecuteMillisecond() {
        return maxExecuteMillisecond;
    }

    public Boolean isShowExecuteTime() {
        return showExecuteTime;
    }

    private Boolean isAllowCurrentPath(String currentPath) {
        return pathFilterHandler.isAllowCurrentPath(currentPath);
    }

    /**
     * 构造SqlBardParameter
     *
     * @param invocation invocation
     * @return SqlBardParameter
     * @throws Exception exception
     */
    private SqlBardParameter buildParameterHandler(Invocation invocation) throws Exception {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        Class<? extends ParameterHandler> clazz = parameterHandler.getClass();

        Field parameterObjectField = clazz.getDeclaredField("parameterObject");
        parameterObjectField.setAccessible(true);
        Object parameterObject = parameterObjectField.get(parameterHandler);

        Field mappedStatementField = clazz.getDeclaredField("mappedStatement");
        mappedStatementField.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) mappedStatementField.get(parameterHandler);

        return new SqlBardParameter(mappedStatement, parameterObject);
    }

    /**
     * 构造SQL变量集合
     * Copy From org.apache.ibatis.scripting.defaults.DefaultParameterHandler
     *
     * @param sqlBardParameter SqlBardParameter
     * @return List
     */
    private List<Object> buildSqlParamList(SqlBardParameter sqlBardParameter) {

        BoundSql boundSql = sqlBardParameter.getBoundSql();
        Configuration configuration = sqlBardParameter.getConfiguration();
        Object parameterObject = sqlBardParameter.getParameterObject();
        TypeHandlerRegistry typeHandlerRegistry = sqlBardParameter.getTypeHandlerRegistry();

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
            completeSql.append(typeConvertHandler.convert(param));
        }
        completeSql.append(new String(sqlArray[sqlParamList.size()]));

        return completeSql.toString();
    }

    /**
     * 输出日志
     *
     * @param sql         sql
     * @param executeTime executeTime
     */
    private void printSqlLog(String sql, Long executeTime) {
        if (!isShowExecuteTime()) {
            log.info("[SQLBard] sql = {}", sql);
        } else if (getMaxExecuteMillisecond() == null || executeTime > getMaxExecuteMillisecond()) {
            log.info("[SQLBard] sql = {} , spend = {}ms", sql, executeTime);
        }
    }

}
