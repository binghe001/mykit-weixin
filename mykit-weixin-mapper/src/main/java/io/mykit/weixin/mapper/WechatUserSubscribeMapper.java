/**
 * Copyright 2018-2118 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mykit.weixin.mapper;

import io.mykit.wechat.mp.beans.xml.receive.event.WxSubscribeEventMessage;
import io.mykit.wechat.mp.beans.xml.receive.event.WxUnSubscribeEventMessage;
import io.mykit.weixin.entity.WechatUserSubscribe;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyazhuang
 * @date 2018/10/29 11:19
 * @description 微信用户关注记录接口
 * @version 1.0.0
 */
public interface WechatUserSubscribeMapper {

    /**
     * 保存用户关注记录
     * @param wechatUserSubscribe 用户关注记录的封装实体
     * @return 保存记录条数
     */
    int saveWechatUserSubscribe(WechatUserSubscribe wechatUserSubscribe);


    /**
     * 更新用户关注记录
     * @param wxSubscribeEventMessage 用户关注记录的封装实体
     * @return 更新记录条数
     */
    int updateWxSubscribeEventMessage(WxSubscribeEventMessage wxSubscribeEventMessage);

    /**
     * 更新用户取消关注状态
     * @param wxUnSubscribeEventMessage 用户取消关注记录的封装实体
     * @return 更新的记录条数
     */
    int updateWxUnSubscribeEventMessage(WxUnSubscribeEventMessage wxUnSubscribeEventMessage);

    /**
     * 更新事件状态
     * @param event 事件状态
     * @param slaveUser 开发者账号
     * @param openId 用户openId
     * @return 更新的记录条数
     */
    int updateEventStatus(@Param("event") String event, @Param("slaveUser") String slaveUser, @Param("openId") String openId);

    /**
     * 根据微信开发者账号和用户openId获取微信关注信息
     * @param slaveUser 微信开发者账号
     * @param openId 微信用户openId
     * @return 微信用户关注信息
     */
    WechatUserSubscribe getWechatUserSubscribe(@Param("slaveUser") String slaveUser, @Param("openId") String openId);

    /**
     * 获取列表
     * @param status 状态
     * @return 返回关注记录列表
     */
    List<WechatUserSubscribe> getWechatUserSubscribes(@Param("status") Integer status);
}
