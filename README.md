# monitor

物联网设备监控中台

物联网设备监控中台作为一个**中台**，对设备运行状况进行实时在线监测、预警，不做业务相关的功能

核心功能列表：

1. 报文数据采集与指标解析 ：整个系统的数据来源是通过接收设备发送过来的报文消息，在系统中定义主题和消息内容字段的指标数据为过滤条件，从而对消息进行收集和分析。
2. 报警监控 ：通过和系统中定义的各种告警级别数据进行对比，一旦发现触发到告警级别的消息，就会通过和告警关联配置的 webhook 来将告警信息透传到其它系统
3. GPS定位监控 ：采集每台设备的GPS定位，并提供设备位置查询功能。
4. 数据看板 : 提供丰富的自定义数据看板。

## 业务架构图

系统从业务上分为 6 大功能模块 ：
1. 图形监控模块
2. 数据详情展示模块
3. 看板管理模块
4. 设备管理模块
5. 报警管理模块
6. 系统管理模块

![业务架构图](https://gitee.com/cpu_code/picture_bed/raw/master/20211001211422.png)


## 技术架构图

![技术架构图](https://gitee.com/cpu_code/picture_bed/raw/master/20211001212536.png)

**预制数据**将放入**MySQL**里进行存储，
设备上报的指标数据包括告警数据将存入**influxDB**中，
设备的**地理位置信息**数据存入到**ES**中以便后期搜索。
为了提高系统的运行稳定性，有些频繁访问的数据储存在**redis**中。


因为考虑到设备上报的数据是非常频繁的，如果单单只依靠MySQL数据库的话，
会很容易将MySQL服务器的CPU的占用率搞到100%，从而会引发整个系统的崩溃无法使用。

一些基本的配置放入到了 Consul 的配置中心，考虑到系统的横向扩展能力，将整个系统基于**Consul**做**注册中心**来搭组建一个微服务。

后面可能会将 Consul 转为 Nacos

## 业务流程图

![业务流程图](https://gitee.com/cpu_code/picture_bed/raw/master/20211001211642.png)


## 功能结构图

![功能结构图](https://gitee.com/cpu_code/picture_bed/raw/master/20211001211927.png)


## 功能列表

<img src="https://gitee.com/cpu_code/picture_bed/raw/master/20211001212121.png" alt="功能列表" style="zoom:200%;" />



## 数据库设计

**管理员表 tb_admin**

| 列名       | 数据类型    | 说明                                          |
| ---------- | ----------- | --------------------------------------------- |
| id         | int         | 表主键id，自增                                |
| login_name | varchar(50) | 登录账号                                      |
| password   | varchar(60) | 密码                                          |
| type       | tinyint     | 类型 1:超级管理员 2:普通用户 目前作为保留字段 |
| board      | varchar(50) | 看板列表                                      |

**指标配置表 tb_quota**

| 列名            | 数据类型      | 说明                                   |
| --------------- | ------------- | -------------------------------------- |
| id              | int           | 表主键id                               |
| name            | varchar(50)   | 指标名称                               |
| unit            | varchar(20)   | 指标单位                               |
| subject         | varchar(50)   | 报文主题                               |
| value_key       | varchar(50)   | 指标值字段                             |
| sn_key          | varchar(50)   | 设备识别码字段                         |
| webhook         | varchar(1000) | web钩子                                |
| value_type      | varchar(10)   | 指标字段类型，Double、Inteter、Boolean |
| reference_value | varchar(100)  | 参考值                                 |

**报警配置表 tb_alarm**

| 列名      | 数据类型      | 说明                     |
| --------- | ------------- | ------------------------ |
| id        | int           | 表主键id，自增           |
| name      | varchar(50)   | 报警指标名称             |
| quota_id  | int           | 关联指标名称             |
| operator  | varchar(10)   | 运算符                   |
| threshold | int           | 报警阈值                 |
| level     | int           | 报警级别 1：一般 2：严重 |
| cycle     | int           | 沉默周期(以分钟为单位)   |
| webhook   | varchar(1000) | web钩子地址              |

**面板配置表 tb_board**

| 列名     | 数据类型     | 说明           |
| -------- | ------------ | -------------- |
| id       | int          | 表主键id，自增 |
| admin_id | int          | 管理员id       |
| name     | varchar(50)  | 看板名称       |
| quota    | varchar(100) | 指标           |
| device   | varchar(100) | 设备           |
| system   | tinyint      | 是否是系统看板 |
| disable  | tinyint      | 是否不显示     |

**GPS配置表 tb_gps**

| 列名       | 数据类型    | 说明                   |
| ---------- | ----------- | ---------------------- |
| id         | bigint      | 表主键id               |
| subject    | varchar(50) | 报文主题               |
| sn_key     | varchar(50) | 设备识别码字段         |
| type       | tinyint     | 类型（单字段、双字段） |
| value_key  | varchar(50) | 经纬度字段             |
| separation | varchar(10) | 经纬度分隔符           |
| longitude  | varchar(20) | 经度字段               |
| latitude   | varchar(20) | 维度字段               |


## 索引库结构设计

**设备库 device**

| 列名      | 数据类型 | 说明     |
| --------- | -------- | -------- |
| deviceId  | keyword  | 设备编号 |
| alarm     | boolean  | 是否告警 |
| alarmName | keyword  | 告警名称 |
| level     | integer  | 告警级别 |
| online    | boolean  | 是否在线 |
| status    | boolean  | 开关     |
| tag       | keyword  | 标签     |