# 作者简介: 
Adam Lu(刘亚壮)，高级软件架构师，Java编程专家，大数据架构师与编程专家，开源分布式消息引擎Mysum发起者、首席架构师及开发者，Android开源消息组件Android-MQ独立作者，国内知名开源分布式数据库中间件Mycat核心架构师、开发者，精通Java, C, C++, Python, Hadoop大数据生态体系，熟悉MySQL、Redis内核，Android底层架构。多年来致力于分布式系统架构、微服务、分布式数据库、大数据技术的研究，曾主导过众多分布式系统、微服务及大数据项目的架构设计、研发和实施落地。在高并发、高可用、高可扩展性、高可维护性和大数据等领域拥有丰富的经验。对Hadoop、Spark、Storm等大数据框架源码进行过深度分析并具有丰富的实战经验。

# 作者联系方式
QQ：2711098650

# 项目简述
基于mykit-wechat-sdk微信SDK开发的微信第三方服务，以 Spring Boot + MyBatis 实现的兼容多微信公众号接入的微信服务

# 项目结构描述

## mykit-weixin-utils
系统通用工具模块

## mykit-wexin-entity
系统的实体模块，封装了系统的实体类、常量和参数

## mykit-weixin-mapper
数据操作模块，以MyBatis操作数据库

## mykit-weixin-service
具体业务逻辑实现

## mykit-weixin-service-api
业务逻辑接口层

## mykit-weixin-controller
服务的controller层，直接对外提供api,其中io.mykit.weixin.WechatPortalController接收微信服务器的响应信息，并通过mykit-wechat-sdk的路由解析功能得出最终结果进行相关的业务逻辑操作

## mykit-weixin-gateway
系统网关，根据规则对系统的请求进行拦截和过滤

## mykit-wexin-core
系统的入口模块，以SpringBoot启动整个项目服务，同时，对系统进行各种配置
注意：在resources目录下，新建redis.properties，重新配置了Redis信息，覆盖了mykit-wechat-sdk下的redis配置redis.properties








