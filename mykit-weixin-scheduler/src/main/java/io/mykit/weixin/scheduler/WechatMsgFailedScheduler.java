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
package io.mykit.weixin.scheduler;

import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.service.WechatTemplateMsgFailedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author binghe
 * @version 1.0.0
 * @description 定时处理模板消息发送失败的记录
 */
@Component
public class WechatMsgFailedScheduler {

    private final Logger logger = LoggerFactory.getLogger(WechatMsgFailedScheduler.class);

    @Resource
    private WechatTemplateMsgFailedService wechatTemplateMsgFailedService;
    /**
     * 每隔5分钟执行一次
     */
    @Scheduled(cron="0 0/5 * * * ?")
    public void handlerWechatTemplateMsgFailed(){
        logger.info("处理微信模板消息发送失败记录开始...");
        wechatTemplateMsgFailedService.handlerWechatTemplateMsgFailed(WechatConstants.BEFORE_DAY);
        logger.info("处理微信模板消息发送失败记录结束...");
    }
}
