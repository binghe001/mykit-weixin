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
import io.mykit.weixin.entity.WechatTemplateMsgFailed;
import io.mykit.weixin.mapper.WechatTemplateMsgFailedMapper;
import io.mykit.weixin.params.WechatTemplateParams;
import io.mykit.weixin.service.WechatTemplateMsgFailedService;
import io.mykit.weixin.service.WechatTemplateService;
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
public class WechatTemplateMsgFailedServiceImpl extends WechatCacheServiceImpl implements WechatTemplateMsgFailedService {
    private final Logger logger = LoggerFactory.getLogger(WechatTemplateMsgFailedServiceImpl.class);
    @Resource
    private WechatTemplateMsgFailedMapper wechatTemplateMsgFailedMapper;
    @Resource
    private WechatTemplateService wechatTemplateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveWechatTemplateMsgFailed(String parameter, Integer errCode, String errMsg, Integer maxRetryCount, Integer currentRetryCount) {
        WechatTemplateMsgFailed wechatTemplateMsgFailed = new WechatTemplateMsgFailed();
        wechatTemplateMsgFailed.setErrCode(errCode);
        wechatTemplateMsgFailed.setErrMsg(errMsg);
        wechatTemplateMsgFailed.setParameter(parameter);
        wechatTemplateMsgFailed.setMaxRetryCount(maxRetryCount);
        wechatTemplateMsgFailed.setCurrentRetryCount(currentRetryCount);
        return wechatTemplateMsgFailedMapper.saveWechatTemplateMsgFailed(wechatTemplateMsgFailed);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlerWechatTemplateMsgFailed(int hour) {
        Calendar calendar = Calendar.getInstance();
        String endDate = DateUtils.parseDateToString(calendar.getTime(), DateUtils.DATE_FORMAT);
        calendar.add(Calendar.HOUR, -hour);
        String startDate = DateUtils.parseDateToString(calendar.getTime(), DateUtils.DATE_FORMAT);

        //获取数据
        List<WechatTemplateMsgFailed> list = wechatTemplateMsgFailedMapper.getWechatTemplateMsgFailedListByTime(startDate, endDate);
        if(list != null && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                WechatTemplateMsgFailed msgFailed = list.get(i);
                String parameter = msgFailed.getParameter();
                try{
                    WechatTemplateParams params = JsonUtils.json2Bean(parameter, WechatTemplateParams.class);
                    //设置为重试
                    params.setRetry(WechatConstants.RETRY_TRUE);
                    //再次重新发送
                    wechatTemplateService.sendWechatTemplateMessage(params);
                    //如果没抛出异常，则说明发送成功，删除当前失败记录
                    wechatTemplateMsgFailedMapper.deleteWechatTemplateMsgFailed(msgFailed.getId());
                }catch (MyException e){
                    logger.info("处理数据失败，状态码为===>>>" + e.getCode() + ",  错误信息为===>>>" + e.getMessage());
                    //当前重试次数+1
                    wechatTemplateMsgFailedMapper.updateCurrentRetryCount(msgFailed.getCurrentRetryCount() + 1, DateUtils.parseDateToString(new Date(), DateUtils.DATE_TIME_FORMAT), msgFailed.getId());
                    continue;
                }catch (Exception e){
                    //当前重试次数+1
                    wechatTemplateMsgFailedMapper.updateCurrentRetryCount(msgFailed.getCurrentRetryCount() + 1, DateUtils.parseDateToString(new Date(), DateUtils.DATE_TIME_FORMAT), msgFailed.getId());
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }
}
