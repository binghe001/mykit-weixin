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

import io.mykit.weixin.entity.WechatUserInfo;
import io.mykit.weixin.service.base.WechatCacheService;

/**
 * @author liuyazhuang
 * @date 2018/10/30 15:12
 * @description 微信用户信息Service
 * @version 1.0.0
 */
public interface WechatUserInfoService extends WechatCacheService {

    /**
     * 保存微信基本信息
     * @param code 微信code
     * @param foreignSystemId 机构在业务系统中的id
     * @param foreignSystem 业务系统的标识
     * @param foreignId 用户在业务系统中的id
     * @param foreignType 用户在业务系统中的类型
     * @return 保存的数据记录条数
     */
    int saveWechatUserInfo(String code, String foreignSystemId, String foreignSystem, String foreignId, String foreignType) throws Exception;


    /**
     * 获取微信用户的微信openId
     * @param foreignSystemId 其他机构在业务系统中的标识
     * @param foreignSystem 业务系统的唯一标识
     * @param foreignId 用户在业务系统中的id
     * @param foreignType 用户在业务系统中的类型
     * @return 用户的openid
     */
    String getOpenId(String foreignSystemId, String foreignSystem, String foreignId, String foreignType);
    /**
     * 获取微信用户的微信openId
     * @param foreignSystemId 其他机构在业务系统中的标识
     * @param foreignSystem 业务系统的唯一标识
     * @param foreignId 用户在业务系统中的id
     * @return 用户的openid
     */
    String getOpenId(String foreignSystemId, String foreignSystem, String foreignId);


    /**
     * 获取微信用户信息
     * @param foreignSystemId 其他机构在业务系统中的标识
     * @param foreignSystem 业务系统的唯一标识
     * @param foreignId 用户在业务系统中的id
     * @param foreignType 用户在业务系统中的类型
     * @return 微信用户完整信息
     */
    WechatUserInfo getWechatUserInfo(String foreignSystemId, String foreignSystem, String foreignId, String foreignType);

}
