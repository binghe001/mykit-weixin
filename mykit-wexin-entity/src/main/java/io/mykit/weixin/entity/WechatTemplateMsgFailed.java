/**
 * Copyright 2019-2999 the original author or authors.
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
 * @author binghe
 * @version 1.0.0
 * @description 模板消息发送失败记录，需要重试的
 */
@Data
public class WechatTemplateMsgFailed extends MonthShardingEntity {
    private static final long serialVersionUID = -5431497875499353970L;

    /**
     * 其他业务系统传递的所有参数
     */
    private String parameter;

    /**
     * 状态码
     */
    private Integer errCode;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount;

    /**
     * 当前重试次数
     */
    private Integer currentRetryCount;

}
