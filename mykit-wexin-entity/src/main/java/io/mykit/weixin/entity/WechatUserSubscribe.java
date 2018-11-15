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
 * @date 2018/10/29 10:36
 * @description 微信关注表记录表, 当确认为关注的时候才为用户推送相关信息
 * @version 1.0.0
 */
@Data
public class WechatUserSubscribe extends MonthShardingEntity {
    private static final long serialVersionUID = 6362284099523684177L;

    /**
     * 机构对应的slaveUser
     */
    private String slaveUser;

    /**
     * 用户的openId
     */
    private String openId;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
     */
    private String event;

}
