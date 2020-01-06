# 作者简介: 
冰河，高级软件架构师，Java编程专家，大数据架构师与编程专家，信息安全高级工程师，Mykit系列开源框架创始人、核心架构师和开发者，Android开源消息组件Android-MQ独立作者，国内知名开源分布式数据库中间件Mycat核心架构师、开发者，精通Java, C, C++, Python, Hadoop大数据生态体系，熟悉MySQL、Redis内核，Android底层架构。多年来致力于分布式系统架构、微服务、分布式数据库、分布式事务与大数据技术的研究，曾主导过众多分布式系统、微服务及大数据项目的架构设计、研发和实施落地。在高并发、高可用、高可扩展性、高可维护性和大数据等领域拥有丰富的架构经验。对Hadoop、Spark、Storm、Flink等大数据框架源码进行过深度分析并具有丰富的实战经验。《海量数据处理与大数据技术实战》、《MySQL开发、优化与运维实战》作者。

# 作者联系方式
QQ：2711098650

# 项目简述
基于mykit-wechat-sdk微信SDK开发的微信第三方服务，以 Spring Boot + MyBatis 实现的兼容多微信公众号接入的微信服务

# 项目结构描述

## mykit-weixin-utils
系统通用工具模块

## mykit-weixin-config
系统配置模块

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

## mykit-weixin-scheduler
定时业务调度模块

## mykit-weixin-test
单元测试模块

# 扫一扫关注微信公账号并加入知识星球

你在刷抖音，玩游戏的时候，别人都在这里学习，成长，提升，人与人最大的差距其实就是思维。你可能不信，优秀的人，总是在一起。  
  
扫一扫关注微信公众号  
![微信公众号](https://github.com/sunshinelyz/binghe_resources/blob/master/images/subscribe/qrcode_for_gh_0d4482676600_344.jpg)  
  
扫一扫加入知识星球  
![知识星球](https://github.com/sunshinelyz/binghe_resources/blob/master/images/subscribe/xq_20200105203129.png)  





