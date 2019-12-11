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
package io.mykit.weixin.service.impl;

import io.mykit.wechat.utils.common.DateUtils;
import io.mykit.wechat.utils.json.JsonUtils;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatKfaccountTextMsgFailed;
import io.mykit.weixin.mapper.WechatKfaccountTextMsgFailedMapper;
import io.mykit.weixin.params.WechatKfaccountTextMsgParams;
import io.mykit.weixin.service.WechatKfaccountTextMsgFailedService;
import io.mykit.weixin.service.WechatKfaccountTextMsgLogService;
import io.mykit.weixin.service.impl.base.WechatCacheServiceImpl;
import io.mykit.weixin.utils.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author binghe
 * @version 1.0.0
 * @description 模板消息发送失败记录处理业务
 */
@Service
public class WechatKfaccountTextMsgFailedServiceImpl extends WechatCacheServiceImpl implements WechatKfaccountTextMsgFailedService {
    private final Logger logger = LoggerFactory.getLogger(WechatKfaccountTextMsgFailedServiceImpl.class);
    @Resource
    private WechatKfaccountTextMsgFailedMapper wechatKfaccountTextMsgFailedMapper;
    @Resource
    private WechatKfaccountTextMsgLogService wechatKfaccountTextMsgLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveWechatKfaccountTextMsgFailed(String parameter, Integer errCode, String errMsg, Integer maxRetryCount, Integer currentRetryCount) {
        WechatKfaccountTextMsgFailed wechatKfaccountTextMsgFailed = new WechatKfaccountTextMsgFailed();
        wechatKfaccountTextMsgFailed.setParameter(parameter);
        wechatKfaccountTextMsgFailed.setErrCode(errCode);
        wechatKfaccountTextMsgFailed.setErrMsg(errMsg);
        wechatKfaccountTextMsgFailed.setMaxRetryCount(maxRetryCount);
        wechatKfaccountTextMsgFailed.setCurrentRetryCount(currentRetryCount);
        return wechatKfaccountTextMsgFailedMapper.saveWechatKfaccountTextMsgFailed(wechatKfaccountTextMsgFailed);
    }

    @Override
    public void handlerWechatKfaccountTextMsgFailed(int hour) {
        Calendar calendar = Calendar.getInstance();
        String endDate = DateUtils.parseDateToString(calendar.getTime(), DateUtils.DATE_FORMAT);
        calendar.add(Calendar.HOUR, -hour);
        String startDate = DateUtils.parseDateToString(calendar.getTime(), DateUtils.DATE_FORMAT);

        //获取数据
        List<WechatKfaccountTextMsgFailed> list = wechatKfaccountTextMsgFailedMapper.getWechatKfaccountTextMsgFailedListByTime(startDate, endDate);
        for(int i = 0; i < list.size(); i++){
            WechatKfaccountTextMsgFailed msgFailed = list.get(i);
            String parameter = msgFailed.getParameter();
            try{
                WechatKfaccountTextMsgParams params = JsonUtils.json2Bean(parameter, WechatKfaccountTextMsgParams.class);
                //设置为重试
                params.setRetry(WechatConstants.RETRY_TRUE);
                //再次发送微信客服消息
                wechatKfaccountTextMsgLogService.sendWechatKfaccountTextMsg(params);
                //没有抛出异常，说明成功，删除记录
                wechatKfaccountTextMsgFailedMapper.deleteWechatKfaccountTextMsgFailed(msgFailed.getId());
            }catch (MyException e){
                logger.info("处理客服消息数据失败，状态码为===>>>" + e.getCode() + ",  错误信息为===>>>" + e.getMessage() + ", id===>>>" + msgFailed.getId());
                //当前重试次数+1
                wechatKfaccountTextMsgFailedMapper.updateCurrentRetryCount(msgFailed.getCurrentRetryCount() + 1, DateUtils.parseDateToString(new Date(), DateUtils.DATE_TIME_FORMAT), msgFailed.getId());
                continue;
            }catch (Exception e){
                //当前重试次数+1
                wechatKfaccountTextMsgFailedMapper.updateCurrentRetryCount(msgFailed.getCurrentRetryCount() + 1, DateUtils.parseDateToString(new Date(), DateUtils.DATE_TIME_FORMAT), msgFailed.getId());
                continue;
            }

        }
    }
}
