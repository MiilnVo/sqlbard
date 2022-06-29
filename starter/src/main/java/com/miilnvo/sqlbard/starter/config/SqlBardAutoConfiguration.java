package com.miilnvo.sqlbard.starter.config;

import com.miilnvo.sqlbard.core.interceptor.SqlBardInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author MiilnVo
 * @since 2022-06-28
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class})
@EnableConfigurationProperties(SqlBardProperties.class)
public class SqlBardAutoConfiguration implements InitializingBean {

    private List<SqlSessionFactory> sqlSessionFactoryList;

    private SqlBardProperties sqlBardProperties;

    public SqlBardAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList, SqlBardProperties sqlBardProperties) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
        this.sqlBardProperties = sqlBardProperties;
    }

    @Override
    public void afterPropertiesSet() {
        if (sqlBardProperties.isEnabled()) {
            SqlBardInterceptor sqlBardInterceptor = new SqlBardInterceptor();
            sqlBardInterceptor.setProperties(sqlBardProperties);
            for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
                sqlSessionFactory.getConfiguration().addInterceptor(sqlBardInterceptor);
            }
        }
    }

}
