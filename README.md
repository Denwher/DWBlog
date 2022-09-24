# DWBlog
## 一、项目概述

>该项目是一个较为完整的博客系统，具备了常见的博客功能，初衷就是为了解决日常的学习记录与总结。
>
>该博客系统大概包含了以下功能：用户的登录与注册、系统的权限管理、发表编辑博客、博客查阅、博客删除、博客评论、个人信息页展示、个人信息的修改、首页信息显示如热门博客、博客置顶以及所有博客列表，同时会做阅读量的统计，还支持关键字和分类搜索，可以快速查找出想要的博客内容。
>
>当然还为管理员提供了后台管理系统，用来管理与维护博客系统，如用户管理、分类管理、标签管理、博客管理等。

## 二、组织结构

```
DWBlog -- 父工程，打包方式为pom，统一锁定依赖的版本，同时聚合其他子模块便于统一执行maven命令
├── denwher-framework -- 核心业务模块，打包方式为jar，存放项目业务逻辑代码，也包括一些工具类、实体类、返回结果、异常处理和常量类
├── denwher-blog -- 博客前台模块，打包方式为war，存放前台的controller类、配置类、过滤器和定时任务等
└── denwher-admin-- 博客后台模块，打包方式为war，存放后台的controller类、配置类、过滤器等

前端工程 -- 分为前台工程和后台工程
├── dw-blog-vue -- 博客前台工程，存放html、css、js、图片等静态资源，和发送异步请求的ajax代码，与denwher-blog模块交互
└── dw-vue-admin -- 博客后台工程，存放html、css、js、图片等静态资源，和发送异步请求的ajax代码，与denwher-admin模块交互
```

## 三、技术选型

- 后端技术栈

|      技术       |      说明      |                             官网                             |
| :-------------: | :------------: | :----------------------------------------------------------: |
|   SpringBoot    |  容器+MVC框架  |            https://spring.io/projects/spring-boot            |
| Spring Security | 认证和授权框架 |          https://spring.io/projects/spring-security          |
|       JWT       |  JWT登录支持   |                 https://github.com/jwtk/jjwt                 |
|   MybatisPlus   |    ORM框架     |                    https://baomidou.com/                     |
|      Redis      |   分布式缓存   |                      https://redis.io/                       |
|    EasyCode     |  逆向代码生成  |             https://gitee.com/makejava/EasyCode/             |
|       OSS       |    对象存储    |             https://www.qiniu.com/products/kodo              |
|    Swagger2     |  文档生成工具  |          https://github.com/swagger-api/swagger-ui           |
|    fastjson     |   数据序列化   |              https://gitee.com/wenshao/fastjson              |
|    EasyExcel    | Excel处理工具  | https://easyexcel.opensource.alibaba.com/docs/current/quickstart |
|      Maven      |  项目构建管理  |                   http://maven.apache.org/                   |

- 前端技术栈

|  技术   |      说明       |                          官网                           |
| :-----: | :-------------: | :-----------------------------------------------------: |
| Vue.js  | JavaScript 框架 |                  https://cn.vuejs.org/                  |
| Element |     组件库      | https://element.eleme.cn/#/zh-CN/component/installation |



## 四、功能架构





## 五、环境搭建

### 1. 开发工具

|     工具      |        说明         |                         官网                          |
| :-----------: | :-----------------: | :---------------------------------------------------: |
|     IDEA      |       开发IDE       |        https://www.jetbrains.com/idea/download        |
|    Navicat    |   数据库连接工具    |          http://www.formysql.com/xiazai.html          |
| PowerDesigner |   数据库设计工具    |               http://powerdesigner.de/                |
| RedisDesktop  | redis客户端连接工具 | https://github.com/qishibo/AnotherRedisDesktopManager |
|    Postman    |   API接口调试工具   |               https://www.postman.com/                |
|    Typora     |   Markdown编辑器    |                  https://typora.io/                   |

### 2. 开发环境

| 工具  | 版本号 |                             下载                             |
| :---: | :----: | :----------------------------------------------------------: |
|  JDK  |  1.8   | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
| Mysql |  5.7   |                    https://www.mysql.com/                    |
| Redis |  5.0+  |                  https://redis.io/download                   |

### 3. Maven项目搭建

- 统一管理依赖版本

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.dengwei</groupId>
    <artifactId>DWBlog</artifactId>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>denwher-framework</module>
        <module>denwher-admin</module>
        <module>denwher-blog</module>
    </modules>
    <packaging>pom</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- SpringBoot的依赖配置-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.5.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--fastjson依赖-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>1.2.33</version>
            </dependency>
            <!--jwt依赖-->
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt</artifactId>
                <version>0.9.0</version>
            </dependency>
            <!--mybatisPlus依赖-->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>3.4.3</version>
            </dependency>

            <!--阿里云OSS-->
            <dependency>
                <groupId>com.aliyun.oss</groupId>
                <artifactId>aliyun-sdk-oss</artifactId>
                <version>3.10.2</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>easyexcel</artifactId>
                <version>3.0.5</version>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>2.9.2</version>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>2.9.2</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```



## 六、数据库设计

![博客ER图](img\博客ER图.jpg)
