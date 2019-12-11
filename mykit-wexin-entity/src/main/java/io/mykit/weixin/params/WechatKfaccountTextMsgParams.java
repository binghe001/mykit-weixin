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
package io.mykit.weixin.params;

import lombok.Data;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2019/5/8
 * @description 微信客服文本消息
 */
@Data
public class WechatKfaccountTextMsgParams extends WechatKfaccountMsgParams {
    private static final long serialVersionUID = -6134126025999753372L;
    /**
     * 发送的消息内容
     */
    private String content;

    /**
     * 是否重试，默认为否
     */
    private String retry = "retry_false";
}
