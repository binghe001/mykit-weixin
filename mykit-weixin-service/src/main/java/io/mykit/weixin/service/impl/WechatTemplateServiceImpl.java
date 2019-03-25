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
package io.mykit.weixin.service.impl;

import io.mykit.wechat.mp.beans.json.template.WxTemplateDataItemSend;
import io.mykit.wechat.mp.beans.json.template.send.WxTemplateDataSend;
import io.mykit.wechat.mp.beans.json.template.send.WxTemplateSend;
import io.mykit.wechat.mp.http.handler.template.send.WxTemplateSendHandler;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.json.JsonUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.entity.WechatTemplate;
import io.mykit.weixin.entity.WechatTemplateMsgLog;
import io.mykit.weixin.mapper.WechatTemplateMapper;
import io.mykit.weixin.mapper.WechatTemplateMsgLogMapper;
import io.mykit.weixin.params.WechatTemplateParams;
import io.mykit.weixin.service.WechatAccountService;
import io.mykit.weixin.service.WechatTemplateService;
import io.mykit.weixin.service.WechatUserInfoService;
import io.mykit.weixin.service.impl.base.WechatCacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author liuyazhuang
 * @date 2018/10/30 20:17
 * @description
 * @version 1.0.0
 */
@Service
public class WechatTemplateServiceImpl extends WechatCacheServiceImpl implements WechatTemplateService {
    private final Logger logger = LoggerFactory.getLogger(WechatTemplateServiceImpl.class);
    @Resource
    private WechatTemplateMapper wechatTemplateMapper;
    @Resource
    private WechatTemplateMsgLogMapper wechatTemplateMsgLogMapper;
    @Resource
    private WechatAccountService wechatAccountService;
    @Resource
    private WechatUserInfoService wechatUserInfoService;

    @Override
    public WechatTemplate getWechatTemplateByType(String type, String accountId) {
        String key = type.concat(accountId).concat("getWechatTemplateByType");
        WechatTemplate wechatTemplate = null;
        String result = getJedisCluster().get(key);
        if(StringUtils.isEmpty(result) || WechatConstants.NULL_STRING.equalsIgnoreCase(result)){
            wechatTemplate = wechatTemplateMapper.getWechatTemplateByType(type, accountId);
            getJedisCluster().setex(key, WechatConstants.WECHAT_CACHE_TIME, JsonUtils.bean2Json(wechatTemplate));
        }else{
            wechatTemplate = JsonUtils.json2Bean(result, WechatTemplate.class);
        }
        return wechatTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sendWechatTemplateMessage(WechatTemplateParams wechatTemplateParams) throws Exception {
        WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(wechatTemplateParams.getForeignSystemId(), wechatTemplateParams.getForeignSystem());
        if(wechatAccount == null){
            logger.info("未获取到微信开发者账号信息....");
            return MobileHttpCode.HTTP_NOT_GET_WECHAT_ACCOUNT;
        }
        //获取微信模板消息
        logger.info(JsonUtils.bean2Json(wechatAccount));
        WechatTemplate wechatTemplate = this.getWechatTemplateByType(wechatTemplateParams.getTemplatetType(), wechatAccount.getId());
        if(wechatTemplate == null){
            logger.info("未获取到微信消息模板....");
            return MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE;
        }
        String openId = wechatUserInfoService.getOpenId(wechatTemplateParams.getForeignSystemId(), wechatTemplateParams.getForeignSystem(), wechatTemplateParams.getForeignId(), wechatTemplateParams.getForeignType());
        if(StringUtils.isEmpty(openId)){
            logger.info("未获取到微信openid....");
            return MobileHttpCode.HTTP_NOT_GET_WECHAT_OPEN_ID;
        }
        WxTemplateDataSend wxTemplateDataSend = new WxTemplateDataSend();
        wxTemplateDataSend.setKeyword1(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword1(), "#173177"));
        wxTemplateDataSend.setKeyword2(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword2(), "#173177"));
        wxTemplateDataSend.setKeyword3(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword3(), "#173177"));
        wxTemplateDataSend.setKeyword4(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword4(), "#173177"));
        wxTemplateDataSend.setRemark(new WxTemplateDataItemSend(wechatTemplateParams.getRemark(), "#173177"));

        WxTemplateSend wxTemplateSend = new WxTemplateSend();
        wxTemplateSend.setData(wxTemplateDataSend);
        wxTemplateSend.setTemplate_id(wechatTemplate.getWechatTemplateId());
        wxTemplateSend.setTouser(openId);
        wxTemplateSend.setUrl(wechatTemplateParams.getUrl());
        //发送模板消息
        String result = WxTemplateSendHandler.sendTemplate(wechatAccount.getAppId(), wechatAccount.getAppSecret(), wxTemplateSend);

        WechatTemplateMsgLog wechatTemplateMsgLog = new WechatTemplateMsgLog();
        wechatTemplateMsgLog.setAccountId(wechatAccount.getId());
        wechatTemplateMsgLog.setTemplateId(wechatTemplate.getId());
        wechatTemplateMsgLog.setType(wechatTemplate.getType());
        wechatTemplateMsgLog.setWechatTemplateId(wechatTemplate.getWechatTemplateId());
        wechatTemplateMsgLog.setParameter(JsonUtils.bean2Json(wechatTemplateParams));
        wechatTemplateMsgLog.setTitle(wechatTemplate.getTitle());
        wechatTemplateMsgLog.setContent(wechatTemplate.getContent());
        wechatTemplateMsgLog.setResult(result);
        int count = wechatTemplateMsgLogMapper.saveWechatTemplateMsgLog(wechatTemplateMsgLog);
        return count > 0 ? MobileHttpCode.HTTP_NORMAL : MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sendWechatTemplateMessageV2(WechatTemplateParams wechatTemplateParams) throws Exception {
        WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(wechatTemplateParams.getForeignSystemId(), wechatTemplateParams.getForeignSystem());
        if(wechatAccount == null){
            logger.info("未获取到微信开发者账号信息....");
            return MobileHttpCode.HTTP_NOT_GET_WECHAT_ACCOUNT;
        }
        //获取微信模板消息
        logger.info(JsonUtils.bean2Json(wechatAccount));
        WechatTemplate wechatTemplate = this.getWechatTemplateByType(wechatTemplateParams.getTemplatetType(), wechatAccount.getId());
        if(wechatTemplate == null){
            logger.info("未获取到微信消息模板....");
            return MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE;
        }
        String openId = wechatUserInfoService.getOpenId(wechatTemplateParams.getForeignSystemId(), wechatTemplateParams.getForeignSystem(), wechatTemplateParams.getForeignId(), wechatTemplateParams.getForeignType());
        if(StringUtils.isEmpty(openId)){
            logger.info("未获取到微信openid....");
            return MobileHttpCode.HTTP_NOT_GET_WECHAT_OPEN_ID;
        }
        WxTemplateDataSend wxTemplateDataSend = new WxTemplateDataSend();
        wxTemplateDataSend.setKeyword1(new WxTemplateDataItemSend(wechatTemplateParams.getFirst(), "#173177"));
        if(wechatTemplateParams.getKeywordCount() >= 1){
            wxTemplateDataSend.setKeyword1(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword1(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 2){
            wxTemplateDataSend.setKeyword2(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword2(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 3){
            wxTemplateDataSend.setKeyword3(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword3(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 4){
            wxTemplateDataSend.setKeyword4(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword4(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 5){
            wxTemplateDataSend.setKeyword5(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword5(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 6){
            wxTemplateDataSend.setKeyword6(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword6(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 7){
            wxTemplateDataSend.setKeyword7(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword7(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 8){
            wxTemplateDataSend.setKeyword8(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword8(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 9){
            wxTemplateDataSend.setKeyword9(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword9(), "#173177"));
        }

        if(wechatTemplateParams.getKeywordCount() >= 10){
            wxTemplateDataSend.setKeyword10(new WxTemplateDataItemSend(wechatTemplateParams.getKeyword10(), "#173177"));
        }

        wxTemplateDataSend.setRemark(new WxTemplateDataItemSend(wechatTemplateParams.getRemark(), "#173177"));

        WxTemplateSend wxTemplateSend = new WxTemplateSend();
        wxTemplateSend.setData(wxTemplateDataSend);
        wxTemplateSend.setTemplate_id(wechatTemplate.getWechatTemplateId());
        wxTemplateSend.setTouser(openId);
        wxTemplateSend.setUrl(wechatTemplateParams.getUrl());
        //发送模板消息
        String result = WxTemplateSendHandler.sendTemplate(wechatAccount.getAppId(), wechatAccount.getAppSecret(), wxTemplateSend);

        WechatTemplateMsgLog wechatTemplateMsgLog = new WechatTemplateMsgLog();
        wechatTemplateMsgLog.setAccountId(wechatAccount.getId());
        wechatTemplateMsgLog.setTemplateId(wechatTemplate.getId());
        wechatTemplateMsgLog.setType(wechatTemplate.getType());
        wechatTemplateMsgLog.setWechatTemplateId(wechatTemplate.getWechatTemplateId());
        wechatTemplateMsgLog.setParameter(JsonUtils.bean2Json(wechatTemplateParams));
        wechatTemplateMsgLog.setTitle(wechatTemplate.getTitle());
        wechatTemplateMsgLog.setContent(wechatTemplate.getContent());
        wechatTemplateMsgLog.setResult(result);
        int count = wechatTemplateMsgLogMapper.saveWechatTemplateMsgLog(wechatTemplateMsgLog);
        return count > 0 ? MobileHttpCode.HTTP_NORMAL : MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED;
    }
}
