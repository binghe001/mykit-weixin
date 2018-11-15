/*
Navicat MySQL Data Transfer

Source Server         : 10.2.2.231_3306
Source Server Version : 50722
Source Host           : 10.2.2.231:3306
Source Database       : wechat

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2018-11-15 16:05:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mp_wechat
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat`;
CREATE TABLE `mp_wechat` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `t_create_time` varchar(30) DEFAULT '' COMMENT '创建的时间yyyy-MM-dd HH:mm:ss',
  `t_create_date` varchar(20) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(20) DEFAULT '' COMMENT '分片字段yyyyMM',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_last_modify_time` varchar(30) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='其他机构的微信信息';

-- ----------------------------
-- Table structure for mp_wechat_account
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_account`;
CREATE TABLE `mp_wechat_account` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `t_create_time` varchar(30) DEFAULT '' COMMENT '创建的时间yyyy-MM-dd HH:mm:ss',
  `t_create_date` varchar(20) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(20) DEFAULT '' COMMENT '分片字段yyyyMM',
  `mp_foreign_system_id` varchar(32) DEFAULT '' COMMENT '外部的id,其他业务或系统的关联性ID',
  `mp_foreign_system` varchar(32) DEFAULT '' COMMENT '其他系统的唯一标识',
  `mp_slave_user` varchar(50) DEFAULT '' COMMENT '微信开发者账号',
  `mp_token` varchar(64) DEFAULT '' COMMENT '配置到微信的token',
  `mp_app_id` varchar(64) DEFAULT '' COMMENT '微信公账号appId',
  `mp_app_secret` varchar(100) DEFAULT '' COMMENT '微信公众号appSecret',
  `mp_url` varchar(255) DEFAULT '' COMMENT '配置到微信服务器的url',
  `mp_encoding_aes_key` varchar(255) DEFAULT '' COMMENT '消息加解密key',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_last_modify_time` varchar(30) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  PRIMARY KEY (`id`),
  KEY `mp_foreign_system_id` (`mp_foreign_system_id`),
  KEY `mp_foreign_system` (`mp_foreign_system`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='其他机构的微信信息';

-- ----------------------------
-- Table structure for mp_wechat_account_template_join
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_account_template_join`;
CREATE TABLE `mp_wechat_account_template_join` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `account_id` varchar(50) DEFAULT '' COMMENT '微信开发者账号id',
  `template_id` varchar(50) DEFAULT '' COMMENT '消息模板id',
  `template_type` varchar(50) DEFAULT '' COMMENT '微信消息模板类型',
  `wechat_template_id` varchar(100) DEFAULT '' COMMENT '微信上配置的模板消息id',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_create_time` varchar(50) DEFAULT '' COMMENT '创建时间',
  `t_create_date` varchar(50) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(50) DEFAULT '' COMMENT '分片字段yyyyMM',
  `t_last_modify_time` varchar(50) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  KEY `template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信开发者账号与模板消息关联表';

-- ----------------------------
-- Table structure for mp_wechat_template
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_template`;
CREATE TABLE `mp_wechat_template` (
  `id` varchar(32) NOT NULL COMMENT '默认id',
  `t_type` varchar(50) DEFAULT '' COMMENT '类型，唯一标识一个微信模板',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_title` varchar(255) DEFAULT '' COMMENT '模板标题',
  `t_content` varchar(255) DEFAULT '' COMMENT '模板内容',
  `t_status` int(2) DEFAULT '1' COMMENT '状态0：删除1：正常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信模板字典';

-- ----------------------------
-- Table structure for mp_wechat_template_msg_log
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_template_msg_log`;
CREATE TABLE `mp_wechat_template_msg_log` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `t_create_time` varchar(30) DEFAULT '' COMMENT '创建的时间yyyy-MM-dd HH:mm:ss',
  `t_create_date` varchar(20) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(20) DEFAULT '' COMMENT '分片字段yyyyMM',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_last_modify_time` varchar(30) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  `template_id` varchar(50) DEFAULT '' COMMENT '微信模板id',
  `t_type` varchar(50) DEFAULT '' COMMENT '类型',
  `wechat_template_id` varchar(100) DEFAULT '' COMMENT '微信上配置的模板消息id',
  `t_parameter` varchar(1024) DEFAULT '' COMMENT '其他业务系统传递的所有参数',
  `account_id` varchar(50) DEFAULT '' COMMENT '微信开发者账号表id',
  `t_result` varchar(255) DEFAULT '' COMMENT '发送结果',
  `t_title` varchar(255) DEFAULT '' COMMENT '模板标题',
  `t_content` varchar(255) DEFAULT '' COMMENT '模板内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发送模板消息记录表';

-- ----------------------------
-- Table structure for mp_wechat_user_info
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_user_info`;
CREATE TABLE `mp_wechat_user_info` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `t_create_time` varchar(30) DEFAULT '' COMMENT '创建的时间yyyy-MM-dd HH:mm:ss',
  `t_create_date` varchar(20) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(20) DEFAULT '' COMMENT '分片字段yyyyMM',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_last_modify_time` varchar(30) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  `foreign_system_id` varchar(50) DEFAULT '' COMMENT '外部的id,其他业务或系统的关联性ID',
  `foreign_system` varchar(50) DEFAULT '' COMMENT '其他系统的唯一标识',
  `slave_user` varchar(50) DEFAULT '' COMMENT '微信开发者账号',
  `open_id` varchar(50) DEFAULT '' COMMENT '微信用户id',
  `foreign_id` varchar(50) DEFAULT '' COMMENT '用户在其他系统上的id',
  `foreign_type` varchar(50) DEFAULT '' COMMENT '用户在其他系统中的类型',
  `nick_name` varchar(50) DEFAULT '' COMMENT '昵称',
  `t_sex` int(2) DEFAULT '0' COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `t_province` varchar(20) DEFAULT '' COMMENT '省',
  `t_city` varchar(20) DEFAULT '' COMMENT '市',
  `t_country` varchar(50) DEFAULT '' COMMENT '国家',
  `head_imgurl` varchar(255) DEFAULT '' COMMENT '头像',
  `t_privilege` varchar(255) DEFAULT '' COMMENT '用户特权信息',
  `union_id` varchar(50) DEFAULT '' COMMENT '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信用户信息';

-- ----------------------------
-- Table structure for mp_wechat_user_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_user_subscribe`;
CREATE TABLE `mp_wechat_user_subscribe` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `t_create_time` varchar(30) DEFAULT '' COMMENT '创建的时间yyyy-MM-dd HH:mm:ss',
  `t_create_date` varchar(20) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(20) DEFAULT '' COMMENT '分片字段yyyyMM',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_last_modify_time` varchar(30) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  `slave_user` varchar(50) DEFAULT '' COMMENT '微信开发者账号',
  `open_id` varchar(50) DEFAULT '' COMMENT '微信用户openId',
  `msg_type` varchar(50) DEFAULT '' COMMENT '消息类型，event',
  `t_event` varchar(20) DEFAULT '' COMMENT '事件类型，subscribe(订阅)、unsubscribe(取消订阅)',
  PRIMARY KEY (`id`),
  KEY `open_id` (`open_id`),
  KEY `slave_user` (`slave_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信用户关注记录表';
