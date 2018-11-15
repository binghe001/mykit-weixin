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

/**
 * @author liuyazhuang
 * @date 2018/10/29 19:44
 * @description 微信用户信息
 * @version 1.0.0
 */
@Data
public class WechatUserInfo extends MonthShardingEntity {
    private static final long serialVersionUID = -5987578367897882098L;

    /**
     * 外部的id,其他业务或系统的关联性ID
     */
    private String foreignSystemId = "";

    /**
     * 其他系统的唯一标识
     */
    private String foreignSystem = "";

    /**
     * 机构对应的slaveUser
     */
    private String slaveUser = "";

    /**
     * 用户的openId
     */
    private String openId = "";

    /**
     * 用户在其他系统上的id
     */
    private String foreignId = "";

    /**
     * 用户在其他系统中的类型
     */
    private String foreignType = "";

    /**
     * 微信昵称
     */
    private String nickname = "";

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private Integer sex = 0;

    /**
     * 省份
     */
    private String province = "";

    /**
     * 城市
     */
    private String city = "";

    /**
     * 国家
     */
    private String country = "";

    /**
     * 头像
     */
    private String headimgurl = "";

    /**
     * 用户特权信息
     */
    private String privilege = "";


    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    private String unionid = "";
}
