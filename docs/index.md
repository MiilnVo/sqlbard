# SQLBard

[![Maven Central](https://img.shields.io/badge/Maven%20Central-v1.0.0-0077CC.svg)](https://maven-badges.herokuapp.com/maven-central/com.miilnvo.sqlbard/sqlbard)
[![License](https://img.shields.io/badge/License-Apache%202.0-339966.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

SQLBard是一款基于MyBatis的轻量级插件，旨在能如诗般优雅地呈现SQL执行时的细节。

> Bard: [bɑːd] n.吟游诗人

### 插件作用

1. 输出完整SQL日志

2. 输出SQL执行耗时日志

3. （敬请期待...）

### 快速开始

#### 一、Spring Boot项目

（1）在pom.xml中加入sqlbard-starter依赖

```xml
<dependency>
    <groupId>com.miilnvo.sqlbard</groupId>
    <artifactId>sqlbard-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

（2）配置相关信息，默认情况下无需配置
```ymal
splbard:
  enabled: true  # 是否启用SQLBard
```

#### 二、Maven项目

（1）在pom.xml中加入sqlbard-core依赖

```xml
<dependency>
    <groupId>com.miilnvo.sqlbard</groupId>
    <artifactId>sqlbard-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
（2）在MyBatis的配置文件中添加SQLBard拦截器

```xml
<plugins>
    <plugin interceptor="com.miilnvo.SQLBardInterceptor">
        <!-- 配置相关信息，默认情况下无需配置 -->
        <property name="enabled" value="true"/>
    </plugin>
</plugins>
```