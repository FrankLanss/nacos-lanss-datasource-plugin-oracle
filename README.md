<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">nacos oracle plugin v2.4.1</h1>
<h4 align="center">基于nacos2.4.1版本进行Oracle适配的插件  Copyright lanss 2024</h4>
<h4 align="center">license MIT</h4>
<br>
多数据源插件
Nacos从2.2.0版本开始,可通过SPI机制注入多数据源实现插件,并在引入对应数据源实现后,便可在Nacos启动时通过读取application.properties配置文件中spring.datasource.platform配置项选择加载对应多数据源插件。

[点就跳转 nacos多数据源官方文档传送门](https://nacos.io/zh-cn/docs/plugin/datasource-plugin)

![Nacos 插件化实现](https://nacos.io/zh-cn/assets/images/config-old-datasource-983c43e3f82b50d3a802b6f400e0f715.png)

## 使用指南
注意事项说明：中国大陆访问github可能会抽风，请自行科学上网。<br>
本项目为非侵入式插件模块，仅提供Oracle数据源插件适配，不修改nacos项目源码。
### 1. 下载nacos服务端
官网地址：https://nacos.io/
<br>
[点击下载 nacos server2.4.1](https://github.com/alibaba/nacos/releases/download/2.4.1/nacos-server-2.4.1.tar.gz)

<br>

### 2. 自行导入执行oracle脚本
本项目路径：/nacos-lanss-datasource-plugin-oracle/sql/nacos2-oracle.sql
<br>
[点击下载 nacos oracle 11g12c脚本](https://github.com/FrankLanss/nacos-lanss-datasource-plugin-oracle/blob/release/sql/nacos2-oracle.sql)
<br>

### 3.添加目录
#### 3.1 原nacos server目录结构说明
|----nacos
<br>
|--------bin
<br>
|--------conf
<br>
|--------data
<br>
|--------LICENSE
<br>
|--------NOTICE
<br>
|--------target

<br>

#### 3.2 新增plugins目录后
|----nacos
<br>
|--------bin
<br>
|--------conf
<br>
|--------data
<br>
|--------LICENSE
<br>
|--------NOTICE
<br>
|--------plugins
<br>
|--------target

<br>

#### 3.3 plugins目录中放入本项目的发布包以及oracle数据库驱动包
|----nacos
<br>
.
<br>
.
<br>
.
<br>
|--------plugins
<br>
|------------nacos-lanss-datasource-plugin-oracle-2.4.1.jar
<br>
|------------ojdbc8-21.13.0.0.jar
<br>
|------------orai18n-21.13.0.0.jar

<br>

#### 3.4 拓展说明（仅部署可跳过本节内容）
为何是新增plugins目录，而不是其他目录？<br>
答案：在startup.sh脚本中<br>
查看<br>
|----nacos
<br>
|--------bin
<br>
|------------startup.sh
<br>
其中启动脚本中有以下内容：
```shell
JAVA_OPT="${JAVA_OPT} -Dloader.path=${BASE_DIR}/plugins,${BASE_DIR}/plugins/health,${BASE_DIR}/plugins/cmdb,${BASE_DIR}/plugins/selector"
```
在java虚拟机命令中-Dloader.path可指定加载依赖目录。

<br>

### 4. 数据源配置修改
#### 4.1 找到nacos原有配置文件application.properties
|----nacos
<br>
.
<br>
|--------conf
<br>
|------------application.properties

#### 4.2 修改application.properties数据源配置
注意说明：自行调整url中的地址，端口，数据库实例名；以及用户密码等相关信息。<br>
以下内容均为必须项不可缺少：
```properties
spring.sql.init.platform=oracle
db.num=1
db.url.0=jdbc:oracle:thin:@localhost:1521:orcl
db.user.0=oracle用户
db.password.0=oracle用户的密码
db.pool.config.driver-class-name=oracle.jdbc.OracleDriver
db.pool.config.connection-test-query=select 1 from dual
```
