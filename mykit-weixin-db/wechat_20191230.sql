/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : wechat

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-12-31 11:19:14
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
-- Records of mp_wechat
-- ----------------------------

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
  `mp_send_template` varchar(30) DEFAULT '' COMMENT '是否发送模板消息 发送:send_yes  不发送: send_no',
  `mp_send_custom` varchar(30) DEFAULT '' COMMENT '是否发送客服消息 发送:send_yes  不发送: send_no',
  PRIMARY KEY (`id`),
  KEY `mp_foreign_system_id` (`mp_foreign_system_id`) USING BTREE,
  KEY `mp_foreign_system` (`mp_foreign_system`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='其他机构的微信信息';

-- ----------------------------
-- Records of mp_wechat_account
-- ----------------------------
INSERT INTO `mp_wechat_account` VALUES ('1111111111111111000000000', '2018-07-17 00:00:00', '2018-07-17', '201807', '8a8383af60747eb8016096fdb3dd0144', 'system_medcare', 'gh_0e80eba3e7e7', 'medcare', 'wx236aa846a79bdece', '8d0929acb787bdf54be08fde687c71d3', 'http://test.cdmn.com/wechat/portal', 'k4pQRbQdF8N36sxdqy36iNDH8M5psv8T5Ax4rdP435q', '1', '关心堂健康中心', '2018-10-29 15:24:50', 'send_yes', 'send_yes');
INSERT INTO `mp_wechat_account` VALUES ('1111111111111111000000001', '2018-07-17 00:00:00', '2018-07-17', '201807', '111111111111111111', 'system_medcare', 'gh_944c72276289', 'medcare', 'wxf3100cdfd4aee86c', '0a0c64dfafe1fde38400b3889162b86a', 'http://test.cdmn.com/wechat/portal', 'k4pQRbQdF8N36sxdqy36iNDH8M5psv8T5Ax4rdP435q', '1', '测试账号', '2018-10-29 15:24:50', 'send_yes', 'send_yes');

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
  KEY `account_id` (`account_id`) USING BTREE,
  KEY `template_id` (`template_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信开发者账号与模板消息关联表';

-- ----------------------------
-- Records of mp_wechat_account_template_join
-- ----------------------------
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000000', '1111111111111111000000000', '111111111111100000000', 'type_notify_order_pay', '9TRj48By1W1Kzp2qdTViXdHCqrdEanlu4Q7TCxbogp0', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000001', '1111111111111111000000000', '111111111111100000001', 'type_notify_reserve_receive', 'NSw1eBzrv0_6Pgb4gy5kIH1TUqQGtyou0eOkKGFbEoQ', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000002', '1111111111111111000000000', '111111111111100000002', 'type_notify_order_sender', 'Pf230JMZtgEILQ06qiI7TDNfDOR7cANkuCfPbhi60Ho', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000003', '1111111111111111000000000', '111111111111100000003', 'type_notify_pay_success', 'SpPcCawlIIe4uVCs4j9uZLHHM4CLVPjMhcJ031ytXVg', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000004', '1111111111111111000000000', '111111111111100000004', 'type_notify_order_cancel', 'U9RiH435QzfEP36P3S5GZWue9kZorA3ddJtbzAyKv9c', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000005', '1111111111111111000000000', '111111111111100000005', 'type_notify_reserve_check', 'UQGRzYvV9eqonGJIQ7RteRFnrCs9uLiPdHRRnxETqqg', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000006', '1111111111111111000000000', '111111111111100000006', 'type_notify_question_reply', 'ZLz5aA8ncOUkdbB2QmPoDduWrQn63ZBQTroqxdq7JF0', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000007', '1111111111111111000000000', '111111111111100000007', 'type_notify_reserve_timeout', 'aM27wAV3UfBS5SctWrrd2b1MmcsPzP2DRo21znQ01f4', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000008', '1111111111111111000000000', '111111111111100000008', 'type_notify_sign_success', 'eTkhMaliS8-cpRMsM5IOXA6f34sTHuvkDHxNZhAEoL4', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000009', '1111111111111111000000000', '111111111111100000009', 'type_notify_order_end', 'eWAT5FLTp4DHsClmKcyeIKyml4_g7C1EmVnDmRj2N3o', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000010', '1111111111111111000000000', '111111111111100000010', 'type_notify_service_timeout', 'kbXotYCayzsqYT_rQyLhJs0zTWZQUK0nJl_M3b7Ltco', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000011', '1111111111111111000000000', '111111111111100000011', 'type_notify_pay_return', 'lw56hr8NNfMDw3L2XwoMnqOiRlofrkfPFqWtrPQhwAs', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');
INSERT INTO `mp_wechat_account_template_join` VALUES ('111111111111110000012', '1111111111111111000000000', '111111111111100000012', 'type_notify_service_finish', 'uxso9tykVd7pXguaGwmh_C-57YGWPpFyrS_HD-anKJQ', '1', '', '2019-03-25 11:55:00', '2019-03-25', '201903', '2019-03-25 11:55:00');

-- ----------------------------
-- Table structure for mp_wechat_kfaccount_text_msg_failed
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_kfaccount_text_msg_failed`;
CREATE TABLE `mp_wechat_kfaccount_text_msg_failed` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `t_create_time` varchar(30) DEFAULT '' COMMENT '创建的时间yyyy-MM-dd HH:mm:ss',
  `t_create_date` varchar(20) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(20) DEFAULT '' COMMENT '分片字段yyyyMM',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_last_modify_time` varchar(30) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  `t_parameter` text COMMENT '其他业务系统传递的所有参数',
  `err_msg` varchar(1024) DEFAULT '' COMMENT '微信开发者账号表id',
  `err_code` int(10) DEFAULT '0' COMMENT '状态码',
  `max_retry_count` int(10) DEFAULT '0' COMMENT '最大重试次数',
  `current_retry_count` int(10) DEFAULT '0' COMMENT '当前重试次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信客服消息发送失败记录，需要重试的';

-- ----------------------------
-- Records of mp_wechat_kfaccount_text_msg_failed
-- ----------------------------

-- ----------------------------
-- Table structure for mp_wechat_kfaccount_text_msg_log
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_kfaccount_text_msg_log`;
CREATE TABLE `mp_wechat_kfaccount_text_msg_log` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `t_create_time` varchar(30) DEFAULT '' COMMENT '创建的时间yyyy-MM-dd HH:mm:ss',
  `t_create_date` varchar(20) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(20) DEFAULT '' COMMENT '分片字段yyyyMM',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_last_modify_time` varchar(30) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  `account_id` varchar(50) DEFAULT '' COMMENT '微信开发者账号表id',
  `open_id` varchar(50) DEFAULT '' COMMENT '接收模板消息的微信用户openId',
  `t_parameter` text COMMENT '其他业务系统传递的所有参数',
  `wx_parameter` text COMMENT '发送微信客服消息参数',
  `t_result` varchar(255) DEFAULT '' COMMENT '发送微信客服消息微信服务器返回的结果',
  `t_retry` varchar(30) NOT NULL DEFAULT 'retry_false' COMMENT '是否是重试的结果retry_false:否 retry_true:是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='其他机构的微信信息';

-- ----------------------------
-- Records of mp_wechat_kfaccount_text_msg_log
-- ----------------------------

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
-- Records of mp_wechat_template
-- ----------------------------
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000000', 'type_notify_order_pay', '订单支付提醒', '订单支付提醒', '{{first.DATA}}\r\n订单号：{{keyword1.DATA}}\r\n订单金额：{{keyword2.DATA}}\r\n时间：{{keyword3.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000001', 'type_notify_reserve_receive', '预约受理提醒', '预约受理提醒', '{{first.DATA}}\r\n预约项目：{{keyword1.DATA}}\r\n预约时间：{{keyword2.DATA}}\r\n服务状态：{{keyword3.DATA}}\r\n订单号：{{keyword4.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000002', 'type_notify_order_sender', '\r\n订单发货通知', '\r\n订单发货通知', '{{first.DATA}}\r\n订单编号：{{keyword1.DATA}}\r\n商品名称：{{keyword2.DATA}}\r\n交易金额：{{keyword3.DATA}}\r\n发货时间：{{keyword4.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000003', 'type_notify_pay_success', '支付成功通知', '支付成功通知', '{{first.DATA}}\r\n订单号：{{keyword1.DATA}}\r\n订单类型：{{keyword2.DATA}}\r\n订单金额：{{keyword3.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000004', 'type_notify_order_cancel', '订单取消通知', '订单取消通知', '\r\n{{first.DATA}}\r\n订单编号：{{keyword1.DATA}}\r\n预约项目：{{keyword2.DATA}}\r\n订单金额：{{keyword3.DATA}}\r\n预约时间：{{keyword4.DATA}}\r\n服务时间：{{keyword5.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000005', 'type_notify_reserve_check', '\r\n预约确认通知', '\r\n预约确认通知', '{{first.DATA}}\r\n预约人姓名：{{keyword1.DATA}}\r\n预约时间：{{keyword2.DATA}}\r\n预约地址：{{keyword3.DATA}}\r\n预约项目：{{keyword4.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000006', 'type_notify_question_reply', '咨询回复提醒', '咨询回复提醒', '{{first.DATA}}\r\n咨询内容：{{keyword1.DATA}}\r\n回复医生：{{keyword2.DATA}}\r\n回复时间：{{keyword3.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000007', 'type_notify_reserve_timeout', '预约到期提醒', '预约到期提醒', '{{first.DATA}}\r\n预约事项：{{keyword1.DATA}}\r\n预约时间：{{keyword2.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000008', 'type_notify_sign_success', '签约成功通知', '签约成功通知', '{{first.DATA}}\r\n签约居民：{{keyword1.DATA}}\r\n签约医生：{{keyword2.DATA}}\r\n申请时间：{{keyword3.DATA}}\r\n生效时间：{{keyword4.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000009', 'type_notify_order_end', '\r\n订单结束通知', '\r\n订单结束通知', '{{first.DATA}}\r\n咨询医生：{{keyword1.DATA}}\r\n咨询费用：{{keyword2.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000010', 'type_notify_service_timeout', '\r\n服务到期提醒', '\r\n服务到期提醒', '\r\n{{first.DATA}}\r\n服务项目：{{keyword1.DATA}}\r\n服务时间：{{keyword2.DATA}}\r\n到期时间：{{keyword3.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000011', 'type_notify_pay_return', '退款通知', '退款通知', '{{first.DATA}}\r\n退款金额：{{keyword1.DATA}}\r\n时间：{{keyword2.DATA}}\r\n{{remark.DATA}}', '1');
INSERT INTO `mp_wechat_template` VALUES ('111111111111100000012', 'type_notify_service_finish', '服务完成通知', '服务完成通知', '{{first.DATA}}\r\n服务内容：{{keyword1.DATA}}\r\n完成时间：{{keyword2.DATA}}\r\n{{remark.DATA}}', '1');

-- ----------------------------
-- Table structure for mp_wechat_template_msg_failed
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_template_msg_failed`;
CREATE TABLE `mp_wechat_template_msg_failed` (
  `id` varchar(32) NOT NULL COMMENT '默认的id',
  `t_create_time` varchar(30) DEFAULT '' COMMENT '创建的时间yyyy-MM-dd HH:mm:ss',
  `t_create_date` varchar(20) DEFAULT '' COMMENT '创建日期yyyy-MM-dd',
  `t_month_sharding` varchar(20) DEFAULT '' COMMENT '分片字段yyyyMM',
  `t_status` int(2) DEFAULT '1' COMMENT '状态 1：正常 0：删除',
  `t_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `t_last_modify_time` varchar(30) DEFAULT '' COMMENT '最后修改时间 yyyy-MM-dd HH:mm:ss',
  `t_parameter` text COMMENT '其他业务系统传递的所有参数',
  `err_msg` varchar(1024) DEFAULT '' COMMENT '微信开发者账号表id',
  `err_code` int(10) DEFAULT '0' COMMENT '状态码',
  `max_retry_count` int(10) DEFAULT '0' COMMENT '最大重试次数',
  `current_retry_count` int(10) DEFAULT '0' COMMENT '当前重试次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板消息发送失败记录，需要重试的';

-- ----------------------------
-- Records of mp_wechat_template_msg_failed
-- ----------------------------

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
  `t_parameter` text COMMENT '其他业务系统传递的所有参数',
  `account_id` varchar(50) DEFAULT '' COMMENT '微信开发者账号表id',
  `t_result` varchar(255) DEFAULT '' COMMENT '发送结果',
  `t_title` varchar(255) DEFAULT '' COMMENT '模板标题',
  `t_content` varchar(255) DEFAULT '' COMMENT '模板内容',
  `open_id` varchar(50) DEFAULT '' COMMENT '微信用户openid',
  `wx_parameter` text COMMENT '微信参数',
  `t_retry` varchar(30) NOT NULL DEFAULT 'retry_false' COMMENT '是否是重试的结果retry_false:否 retry_true:是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发送模板消息记录表';

-- ----------------------------
-- Records of mp_wechat_template_msg_log
-- ----------------------------

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
-- Records of mp_wechat_user_info
-- ----------------------------

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
  KEY `open_id` (`open_id`) USING BTREE,
  KEY `slave_user` (`slave_user`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信用户关注记录表';

-- ----------------------------
-- Records of mp_wechat_user_subscribe
-- ----------------------------

-- ----------------------------
-- Table structure for mp_wechat_webpage_authorization_log
-- ----------------------------
DROP TABLE IF EXISTS `mp_wechat_webpage_authorization_log`;
CREATE TABLE `mp_wechat_webpage_authorization_log` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信网页授权认证记录';

-- ----------------------------
-- Records of mp_wechat_webpage_authorization_log
-- ----------------------------
