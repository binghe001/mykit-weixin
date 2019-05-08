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
package io.mykit.weixin.mapper;

import io.mykit.weixin.entity.WechatKfaccountTextMsgLog;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2019/5/8
 * @description 发送微信客服消息记录数据库操作接口
 */
public interface WechatKfaccountTextMsgLogMapper {

    /**
     * 保存发送微信客服消息的记录
     * @param wechatKfaccountTextMsgLog 微信客服消息历史记录对象
     * @return 保存数据的记录条数
     */
    int saveWechatKfaccountTextMsgLog(WechatKfaccountTextMsgLog wechatKfaccountTextMsgLog);
}
