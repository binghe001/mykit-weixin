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

import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.mp.beans.json.kfaccount.message.WxKfaccountTextMessage;
import io.mykit.wechat.mp.beans.json.kfaccount.message.WxKfaccountTextMessageItem;
import io.mykit.wechat.mp.http.handler.kfaccount.WxKfaccountHandler;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.constants.WxConstants;
import io.mykit.wechat.utils.json.JsonUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.entity.WechatKfaccountTextMsgLog;
import io.mykit.weixin.mapper.WechatKfaccountTextMsgLogMapper;
import io.mykit.weixin.params.WechatKfaccountTextMsgParams;
import io.mykit.weixin.service.WechatAccountService;
import io.mykit.weixin.service.WechatKfaccountTextMsgLogService;
import io.mykit.weixin.service.WechatUserInfoService;
import io.mykit.weixin.service.impl.base.WechatCacheServiceImpl;
import io.mykit.weixin.utils.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2019/5/8
 * @description 微信客服消息记录Service实现类
 */
@Service
public class WechatKfaccountTextMsgLogServiceImpl extends WechatCacheServiceImpl implements WechatKfaccountTextMsgLogService {
    private final Logger logger = LoggerFactory.getLogger(WechatTemplateServiceImpl.class);
    @Resource
    private WechatAccountService wechatAccountService;
    @Resource
    private WechatUserInfoService wechatUserInfoService;
    @Resource
    private WechatKfaccountTextMsgLogMapper wechatKfaccountTextMsgLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sendWechatKfaccountTextMsg(WechatKfaccountTextMsgParams wechatKfaccountTextMsgParams)  throws Exception{
        WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(wechatKfaccountTextMsgParams.getForeignSystemId(), wechatKfaccountTextMsgParams.getForeignSystem());
        if(wechatAccount == null){
            logger.info("未获取到微信开发者账号信息....");
            throw new MyException("未获取到微信开发者账号信息", MobileHttpCode.HTTP_NOT_GET_WECHAT_ACCOUNT);
        }
        //是否有权限发送模板消息,没有权限发送，直接返回状态码
        if(StringUtils.isEmpty(wechatAccount.getSendCustom()) || WechatConstants.SEND_NO.equals(wechatAccount.getSendCustom())){
            throw new MyException("没有权限发送", MobileHttpCode.HTTP_NO_LIMIT_TO_SEND_TEMPLATE);
        }
        String openId = wechatUserInfoService.getOpenId(wechatKfaccountTextMsgParams.getForeignSystemId(), wechatKfaccountTextMsgParams.getForeignSystem(), wechatKfaccountTextMsgParams.getForeignId(), wechatKfaccountTextMsgParams.getForeignType());
        if(StringUtils.isEmpty(openId)){
            logger.info("未获取到微信openid....");
            throw new MyException("未获取到微信openid", MobileHttpCode.HTTP_NOT_GET_WECHAT_OPEN_ID);
        }
        WxKfaccountTextMessageItem wxKfaccountTextMessageItem = new WxKfaccountTextMessageItem();
        wxKfaccountTextMessageItem.setContent(wechatKfaccountTextMsgParams.getContent());
        WxKfaccountTextMessage wxKfaccountTextMessage = new WxKfaccountTextMessage();
        wxKfaccountTextMessage.setText(wxKfaccountTextMessageItem);
        wxKfaccountTextMessage.setMsgtype(WxConstants.TYPE_TEXT);
        wxKfaccountTextMessage.setTouser(openId);
        //发送微信客服消息
        String ret = "";
        try{
            ret = WxKfaccountHandler.sendWxKfaccountTextMessage(wechatAccount.getAppId(), wechatAccount.getAppSecret(), wxKfaccountTextMessage);
        }catch (Exception e) {
            e.printStackTrace();
            throw new MyException("服务端异常", MobileHttpCode.HTTP_SERVER_EXCEPTION);
        }
        if(StringUtils.isEmpty(ret)){
            throw new MyException("发送微信客服消息失败", MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED);
        }
        JSONObject jsonObject = JSONObject.parseObject(ret);
        if (!jsonObject.containsKey(WechatConstants.WEHCAT_ERROR_CODE)){
            throw new MyException("发送微信客服消息失败", MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED);
        }
        Integer wechatCode = jsonObject.getInteger(WechatConstants.WEHCAT_ERROR_CODE);
        if(wechatCode == null || wechatCode != WechatConstants.WECHAT_CODE_NORMAL){
            throw new MyException("发送微信客服消息失败", MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED);
        }
        //保存记录
        WechatKfaccountTextMsgLog wechatKfaccountTextMsgLog = new WechatKfaccountTextMsgLog();
        wechatKfaccountTextMsgLog.setParameter(JsonUtils.bean2Json(wechatKfaccountTextMsgParams));
        wechatKfaccountTextMsgLog.setWxParameter(JsonUtils.bean2Json(wxKfaccountTextMessage));
        wechatKfaccountTextMsgLog.setResult(ret);
        wechatKfaccountTextMsgLog.setAccountId(wechatAccount.getId());
        wechatKfaccountTextMsgLog.setOpenId(openId);
        wechatKfaccountTextMsgLog.setRetry(wechatKfaccountTextMsgParams.getRetry());
        return wechatKfaccountTextMsgLogMapper.saveWechatKfaccountTextMsgLog(wechatKfaccountTextMsgLog);
    }
}
