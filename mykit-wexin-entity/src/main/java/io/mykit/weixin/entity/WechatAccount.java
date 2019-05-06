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
package io.mykit.weixin.entity;

import io.mykit.weixin.entity.base.MonthShardingEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author liuyazhuang
 * @date 2018/10/8 11:23
 * @description 微信账户
 * @version 1.0.0
 */
@Data
public class WechatAccount extends MonthShardingEntity {
    private static final long serialVersionUID = -1919528766538151384L;

    /**
     * 外部的id,其他业务或系统的关联性ID
     */
    private String foreignSystemId;

    /**
     * 其他系统的唯一标识
     */
    private String foreignSystem;

    /**
     * 机构对应的slaveUser
     */
    private String slaveUser;

    /**
     * 配置到微信公众号的token
     */
    private String token;

    /**
     * 微信公众号appId
     */
    private String appId;

    /**
     * 微信公众号appSecret
     */
    private String appSecret;

    /**
     * 消息加解密Key
     */
    private String encodingAESKey;

    /**
     * 配置到微信服务器的url
     */
    private String url;

    /**
     * 是否发送模板消息
     */
    private String sendTemplate;

    /**
     * 是否发送客服消息
     */
    private String sendCustom;

    public WechatAccount(){
        super(new Date());
    }

    public WechatAccount(String foreignSystemId, String foreignSystem, String slaveUser, String token, String appId, String appSecret, String encodingAESKey, String url, String sendTemplate, String sendCustom) {
        super(new Date());
        this.foreignSystemId = foreignSystemId;
        this.foreignSystem = foreignSystem;
        this.slaveUser = slaveUser;
        this.token = token;
        this.appId = appId;
        this.appSecret = appSecret;
        this.encodingAESKey = encodingAESKey;
        this.url = url;
        this.sendTemplate = sendTemplate;
        this.sendCustom = sendCustom;
    }
}
