package com.miilnvo.sqlbard.core.pojo;

import lombok.Data;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * @author MiilnVo
 * @since 2022-06-30
 */
@Data
public class SqlBardDefaultParameter {

    private TypeHandlerRegistry typeHandlerRegistry;

    private MappedStatement mappedStatement;

    private Object parameterObject;

    private BoundSql boundSql;

    private Configuration configuration;

    public SqlBardDefaultParameter(MappedStatement mappedStatement, Object parameterObject) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = mappedStatement.getBoundSql(parameterObject);
    }

}
