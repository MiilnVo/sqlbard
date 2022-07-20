# SQLBard

[![Maven Central](https://img.shields.io/badge/Maven%20Central-v1.0.0-0077CC.svg)](https://maven-badges.herokuapp.com/maven-central/com.miilnvo.sqlbard/sqlbard)
[![License](https://img.shields.io/badge/License-Apache%202.0-339966.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

http://sqlbard.miilnvo.com

SQLBard是一款基于MyBatis的轻量级插件，旨在能如诗般优雅地呈现SQL执行时的细节

Rendering SQL as graceful as Bard for MyBatis

> Bard: [bɑːd] n.吟游诗人


### 核心功能

```text
[SQLBard] sql = SELECT app_id,app_name,app_key,app_secret FROM application_info WHERE app_id = 1 AND app_key = 'Hnlqhfaf'; , spend = 2ms 
```

1. 输出完整SQL日志

2. 输出SQL执行耗时日志


### 快速开始

#### 一、Spring Boot项目

（1）在pom.xml中添加sqlbard-starter依赖

```xml
<dependency>
    <groupId>com.miilnvo.sqlbard</groupId>
    <artifactId>sqlbard-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

（2）按照需求配置相关属性，默认情况下无需进行任何配置
```yaml
sqlbard:
  # 是否启动: true = 启动，false = 禁用，默认值为true
  enabled: true
  # 是否显示SQL的执行时间: true = 显示，false = 不显示，默认值为false
  showExecuteTime: false
  # 在超过此时间后才显示SQL的执行时间，仅在showExecuteTime为true时有效，单位为毫秒，默认值为-1表示不做限制
  maxExecuteMillisecond: -1
  # 允许插件拦截的路径，多个路径之间用英文逗号(,)分隔，星号(*)表示全匹配，默认值为全部
  allowPathList: com.path1.mapper,com.path2.mapper
  # 不允许插件拦截的路径，多个路径之间用英文逗号(,)分隔，星号(*)表示全匹配，默认值为无
  notAllowPathList: com.path4.mapper,com.path3.*
  # 布尔类型转换策略，默认值为toNumber:
  # toNumber = true转换成1，false转换成0
  # toString = true转换成"true"，false转换成"false"
  booleanStrategy: toNumber
  # 枚举类型转换策略，默认值为toName:
  # toName = 转换成枚举值的名称，即Enum#name()方法的值
  # toOrdinal = 转换成枚举值的下标，即Enum#ordinal()方法的值
  enumStrategy: toName
```

（3）当项目中的SQL执行时即可在日志文件中看到以`[SQLBard]`为前缀的日志

#### 二、Maven项目

（1）在pom.xml中加入sqlbard-core依赖

```xml
<dependency>
    <groupId>com.miilnvo.sqlbard</groupId>
    <artifactId>sqlbard-core</artifactId>
    <version>1.0.0</version>
</dependency>
```
（2）在MyBatis的xml配置文件中添加SQLBard拦截器

```xml
<configuration>
    <plugins>
        <plugin interceptor="com.miilnvo.sqlbard.core.interceptor.SqlBardInterceptor">
            <!-- 按照需求配置相关属性，默认情况下无需进行任何配置 -->
            <!-- 是否启动: true = 启动，false = 禁用，默认值为true -->
            <property name="enabled" value="true"/>
            <!-- 是否显示SQL的执行时间: true = 显示，false = 不显示，默认值为false -->
            <property name="showExecuteTime" value="false"/>
            <!-- 在超过此时间后才显示SQL的执行时间，仅在showExecuteTime为true时有效，单位为毫秒，默认值为-1表示不做限制 -->
            <property name="maxExecuteMillisecond" value="-1"/>
            <!-- 允许插件拦截的路径，多个路径之间用英文逗号(,)分隔，星号(*)表示全匹配，默认值为全部 -->
            <property name="allowPathList" value="com.path1.mapper,com.path2.mapper"/>
            <!-- 不允许插件拦截的路径，多个路径之间用英文逗号(,)分隔，星号(*)表示全匹配，默认值为无 -->
            <property name="notAllowPathList" value="com.path4.mapper,com.path3.*"/>
            <!-- 布尔类型转换策略，默认值为toNumber: -->
            <!-- toNumber = true转换成1，false转换成0 -->
            <!-- toString = true转换成"true"，false转换成"false" -->
            <property name="booleanStrategy" value="toNumber"/>
            <!-- 枚举类型转换策略，默认值为toName: -->
            <!-- toName = 转换成枚举值的名称，即Enum#name()方法的值 -->
            <!-- toOrdinal = 转换成枚举值的下标，即Enum#ordinal()方法的值 -->
            <property name="enumStrategy" value="toName"/>
        </plugin>
    </plugins>
</configuration>
```

（3）当项目中的SQL执行时即可在日志文件中看到以`[SQLBard]`为前缀的日志