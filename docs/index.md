# SQLBard

[![Maven Central](https://img.shields.io/badge/Maven%20Central-v1.0.0-0077CC.svg)](https://maven-badges.herokuapp.com/maven-central/com.miilnvo.sqlbard/sqlbard)
[![License](https://img.shields.io/badge/License-Apache%202.0-339966.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

SQLBard是一款基于MyBatis的轻量级插件，旨在如诗般优雅地呈现完整SQL和执行耗时

Rendering SQL as graceful as Bard for MyBatis

> Bard: [bɑːd] n.吟游诗人

<br/>

### 诞生背景

在日常开发中我们经常需要了解实际执行的SQL是什么，对此MyBatis提供了日志功能，开启后效果如下：

```text
Creating a new SqlSession
SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@638b4eb1] was not registered for synchronization because synchronization is not active
JDBC Connection [io.shardingsphere.shardingjdbc.jdbc.core.connection.MasterSlaveConnection@10d2d4c3] will not be managed by Spring
==>  Preparing: SELECT id,app_id,name,sex,age FROM user_info WHERE app_id = ? AND age = ?;
==> Parameters: XNL(String), 25(Integer)
<==    Columns: id, app_id, name, sex, age
<==        Row: 15, XNL, 小云, 男, 25
<==        Row: 73, XNL, 小星, 男, 25
<==        Row: 82, XNL, 小月, 女, 25
<==      Total: 3
Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@638b4eb1]
```

可以很明显地发现，MyBatis的日志功能存在以下不足：

（1）SQL和参数是分开的，在高I/O和多参数场景下两者难以进行匹配，并且非常不方便复制粘贴到数据库中直接执行

（2）一条SQL会产生N行日志，包括累赘的返回结果集，造成空间上的浪费

**Now! 使用SQLBard便可轻松解决上述问题**

```text
[SQLBard] sql = SELECT id,app_id,name,sex,age FROM user_info WHERE app_id = 'XNL' AND age = 25; , spend = 2ms 
```

<br/>

### 核心特性

* 输出完整SQL日志和执行耗时

* 支持仅出现慢SQL时才输出日志

* 支持拦截指定路径的SQL

* 支持部分特殊类型的多种转换策略

<br/>

### 快速开始

#### 一、Spring Boot项目

1、在pom.xml中添加sqlbard-starter依赖

```xml
<dependency>
    <groupId>com.miilnvo.sqlbard</groupId>
    <artifactId>sqlbard-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

2、在application.yml文件中按照需求配置相关属性，默认情况下无需进行任何配置

```yaml
sqlbard:
  # 是否启动
  enabled: true
  # 是否显示SQL的执行时间
  showExecuteTime: false
  # 在超过此时间后才显示SQL的执行时间
  maxExecuteMillisecond: -1
  # 允许插件拦截的路径
  allowPathList: com.path1.mapper,com.path2.mapper
  # 不允许插件拦截的路径
  notAllowPathList: com.path3.*
  # 布尔类型转换策略
  booleanStrategy: toNumber
  # 枚举类型转换策略
  enumStrategy: toName
```

3、当项目中的SQL执行时即可在日志文件中看到以`[SQLBard]`为前缀的日志

#### 二、Maven项目

1、在pom.xml中加入sqlbard-core依赖

```xml
<dependency>
    <groupId>com.miilnvo.sqlbard</groupId>
    <artifactId>sqlbard-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

2、在MyBatis的xml配置文件中添加SQLBard拦截器

```xml
<configuration>
    <plugins>
        <plugin interceptor="com.miilnvo.sqlbard.core.interceptor.SqlBardInterceptor">
            <!-- 按照需求配置相关属性，默认情况下无需进行任何配置 -->
            <!-- 是否启动 -->
            <property name="enabled" value="true"/>
            <!-- 是否显示SQL的执行时间 -->
            <property name="showExecuteTime" value="false"/>
            <!-- 在超过此时间后才显示SQL的执行时间 -->
            <property name="maxExecuteMillisecond" value="-1"/>
            <!-- 允许插件拦截的路径 -->
            <property name="allowPathList" value="com.path1.mapper,com.path2.mapper"/>
            <!-- 不允许插件拦截的路径 -->
            <property name="notAllowPathList" value="com.path3.*"/>
            <!-- 布尔类型转换策略 -->
            <property name="booleanStrategy" value="toNumber"/>
            <!-- 枚举类型转换策略 -->
            <property name="enumStrategy" value="toName"/>
        </plugin>
    </plugins>
</configuration>
```

3、当项目中的SQL执行时即可在日志文件中看到以`[SQLBard]`为前缀的日志

<br/>

### 配置详解

| 配置项名称            | 配置值                | 默认值   | 说明                                                         |
| :-------------------- | :-------------------- | :------- | :----------------------------------------------------------- |
| enabled               | true<br/>false        | true     | 是否启动                                                     |
| showExecuteTime       | true<br/>false        | false    | 是否显示SQL的执行时间                                        |
| maxExecuteMillisecond | 0 ~ 1000000           | -1       | 在超过此时间后才显示SQL的执行时间，仅在showExecuteTime为true时有效，单位为毫秒 |
| allowPathList         | 具体路径              | 所有     | 允许插件拦截的路径，多个路径之间用英文逗号(,)分隔，星号(*)表示全匹配<br/>例：com.path1.mapper,com.path2.mapper |
| notAllowPathList      | 具体路径              | 无       | 不允许插件拦截的路径，多个路径之间用英文逗号(,)分隔，星号(*)表示全匹配<br/>例：com.path3.\* |
| booleanStrategy       | toNumber<br/>toString | toNumber | 布尔类型转换策略：<br/>toNumber = true转换成1，false转换成0<br/>toString = true转换成"true"，false转换成"false" |
| enumStrategy          | toName<br/>toOrdinal  | toName   | 枚举类型转换策略：<br/>toName = 转换成枚举值的名称，即Enum#name()方法的值<br/>toOrdinal = 转换成枚举值的下标，即Enum#ordinal()方法的值 |

> 类型转换策略：对于部分特殊类型（Boolean、Enum、Date等），需要按照一定策略进行转换后才能输出符合SQL查询时的格式

<br/>

### 示例

在[sqlbard-example](https://github.com/MiilnVo/sqlbard/tree/master/example)模块里

修改里面application.yml文件中的数据库用户名和密码，然后直接运行test目录下的SqlbardExampleApplicationTests，即可在控制台中看到相关日志

<br/>

### 其他

在使用过程中有任何问题、建议、想法，都可以通过`issues`或者`miilnvo@qq.com`进行联系

<br/>
