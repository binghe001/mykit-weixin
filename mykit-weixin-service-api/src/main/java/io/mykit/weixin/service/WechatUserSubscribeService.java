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
package io.mykit.weixin.service;

import com.github.pagehelper.PageInfo;
import io.mykit.wechat.mp.beans.xml.receive.event.WxSubscribeEventMessage;
import io.mykit.wechat.mp.beans.xml.receive.event.WxUnSubscribeEventMessage;
import io.mykit.weixin.entity.WechatUserSubscribe;

/**
 * @author liuyazhuang
 * @date 2018/10/29 15:49
 * @description 微信用户关注记录Service接口
 * @version 1.0.0
 */
public interface WechatUserSubscribeService {

    /**
     * 保存获取更新用户关注信息
     * @param wxSubscribeEventMessage 用户关注的信息
     * @return 返回保存或更新的记录条数
     */
    int saveOrUpdateWxSubscribeEventMessage(WxSubscribeEventMessage wxSubscribeEventMessage);

    /**
     * 更新状态为未关注的事件状态
     * @param wxUnSubscribeEventMessage 用户未关注的信息
     * @return
     */
    int updateWxUnSubscribeEventMessage(WxUnSubscribeEventMessage wxUnSubscribeEventMessage);


    /**
     * 根据微信开发者账号和用户openId获取微信关注信息
     * @param slaveUser 微信开发者账号
     * @param openId 微信用户openId
     * @return 微信用户关注信息
     */
    WechatUserSubscribe getWechatUserSubscribe(String slaveUser, String openId);

    /**
     * 分页获取关注记录信息
     * @param page 当前页码
     * @param pageSize 每页记录数
     * @param status 状态
     * @return 分页信息
     */
    PageInfo<WechatUserSubscribe> queryPage(Integer page, Integer pageSize, Integer status);
}
