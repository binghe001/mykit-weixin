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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.mp.beans.json.kfaccount.message.WxKfaccountTextMessage;
import io.mykit.wechat.mp.beans.json.kfaccount.message.WxKfaccountTextMessageItem;
import io.mykit.wechat.mp.http.handler.kfaccount.WxKfaccountHandler;
import io.mykit.wechat.utils.common.ObjectUtils;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.constants.WxConstants;
import io.mykit.wechat.utils.json.JsonUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.entity.WechatKfaccountTextMsgLog;
import io.mykit.weixin.entity.WechatUserInfo;
import io.mykit.weixin.mapper.WechatKfaccountTextMsgLogMapper;
import io.mykit.weixin.mapper.WechatUserInfoMapper;
import io.mykit.weixin.params.WechatKfaccountNewsMsgParams;
import io.mykit.weixin.params.WechatKfaccountTextMsgParams;
import io.mykit.weixin.params.WechatUserParams;
import io.mykit.weixin.service.WechatAccountService;
import io.mykit.weixin.service.WechatKfaccountTextMsgFailedService;
import io.mykit.weixin.service.WechatKfaccountTextMsgLogService;
import io.mykit.weixin.service.WechatUserInfoService;
import io.mykit.weixin.service.impl.base.WechatCacheServiceImpl;
import io.mykit.weixin.service.task.WxKfaccountNewsMessageTask;
import io.mykit.weixin.utils.exception.MyException;
import io.mykit.weixin.utils.thread.ExecutorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    private WechatUserInfoMapper wechatUserInfoMapper;
    @Resource
    private WechatUserInfoService wechatUserInfoService;
    @Resource
    private WechatKfaccountTextMsgLogMapper wechatKfaccountTextMsgLogMapper;
    @Resource
    private WechatKfaccountTextMsgFailedService wechatKfaccountTextMsgFailedService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendWechatKfaccountTextMsg(WechatKfaccountTextMsgParams wechatKfaccountTextMsgParams)  throws Exception{
        WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(wechatKfaccountTextMsgParams.getForeignSystemId(), wechatKfaccountTextMsgParams.getForeignSystem());
        if(wechatAccount == null){
            logger.info("未获取到微信开发者账号信息....");
            throw new MyException("未获取到微信开发者账号信息", MobileHttpCode.HTTP_NOT_GET_WECHAT_ACCOUNT);
        }
        //是否有权限发送模板消息,没有权限发送，直接返回状态码
        if(StringUtils.isEmpty(wechatAccount.getSendCustom()) || WechatConstants.SEND_NO.equals(wechatAccount.getSendCustom())){
            throw new MyException("没有权限发送", MobileHttpCode.HTTP_NO_LIMIT_TO_SEND_TEMPLATE);
        }
        String sendType = wechatKfaccountTextMsgParams.getSendType();
        if(StringUtils.isEmpty(sendType)){
            sendType = WechatConstants.SEND_SINGLE;
        }
        switch (sendType){
            case WechatConstants.SEND_SINGLE:
                this.sendSingleWechatKfaccountTextMsg(wechatKfaccountTextMsgParams, wechatAccount);
                break;
            case WechatConstants.SEND_MULTI:
                this.sendMutileWechatKfaccountTextMsg(wechatKfaccountTextMsgParams, wechatAccount);
                break;
            default:
                throw new MyException("传递的sendType参数非法," , MobileHttpCode.HTTP_PARAMETER_INVALID);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendWechatKfaccountNewsMsg(WechatKfaccountNewsMsgParams wechatKfaccountNewsMsgParams) throws Exception {
        WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(wechatKfaccountNewsMsgParams.getForeignSystemId(), wechatKfaccountNewsMsgParams.getForeignSystem());
        if(wechatAccount == null){
            logger.info("未获取到微信开发者账号信息....");
            throw new MyException("未获取到微信开发者账号信息", MobileHttpCode.HTTP_NOT_GET_WECHAT_ACCOUNT);
        }
        //是否有权限发送模板消息,没有权限发送，直接返回状态码
        if(StringUtils.isEmpty(wechatAccount.getSendCustom()) || WechatConstants.SEND_NO.equals(wechatAccount.getSendCustom())){
            throw new MyException("没有权限发送", MobileHttpCode.HTTP_NO_LIMIT_TO_SEND_TEMPLATE);
        }
        String sendType = wechatKfaccountNewsMsgParams.getSendType();
        if(StringUtils.isEmpty(sendType)){
            sendType = WechatConstants.SEND_SINGLE;
        }
        switch (sendType){
            case WechatConstants.SEND_SINGLE:
                this.sendSingleWechatKfaccountNewsMsg(wechatKfaccountNewsMsgParams, wechatAccount);
                break;
            case WechatConstants.SEND_MULTI:
                this.sendMutileWechatKfaccountNewsMsg(wechatKfaccountNewsMsgParams, wechatAccount);
                break;
            case WechatConstants.SEND_ALL:
                this.sendAllWechatKfaccountNewsMsg(wechatKfaccountNewsMsgParams, wechatAccount);
                break;
            default:
                throw new MyException("传递的sendType参数非法," , MobileHttpCode.HTTP_PARAMETER_INVALID);
        }

    }
    //发送给单个成员
    private void sendSingleWechatKfaccountNewsMsg(WechatKfaccountNewsMsgParams wechatKfaccountNewsMsgParams, WechatAccount wechatAccount) {
        //TODO 待实现
    }

    //发送给多个成员
    private void sendMutileWechatKfaccountNewsMsg(WechatKfaccountNewsMsgParams wechatKfaccountNewsMsgParams, WechatAccount wechatAccount) {
        //TODO 待实现
    }

    //发送给所有成员
    private void sendAllWechatKfaccountNewsMsg(WechatKfaccountNewsMsgParams wechatKfaccountNewsMsgParams, WechatAccount wechatAccount) {
        WxKfaccountNewsMessageTask task = new WxKfaccountNewsMessageTask(wechatUserInfoMapper, wechatKfaccountNewsMsgParams, wechatAccount);
        ExecutorUtils.executeThread(task);

    }



    /**
     * 向多个用户发送微信消息
     */
    private void sendMutileWechatKfaccountTextMsg(WechatKfaccountTextMsgParams wechatKfaccountTextMsgParams, WechatAccount wechatAccount) {
        String foreignIds = wechatKfaccountTextMsgParams.getForeignId();
        if(StringUtils.isEmpty(foreignIds)){
            throw new MyException("传递的用户业务id为为空," , MobileHttpCode.HTTP_PARAMETER_INVALID);
        }
        JSONArray jsonArray = JSONArray.parseArray(foreignIds);
        List<WechatUserParams> list = jsonArray.toJavaList(WechatUserParams.class);
        if (!ObjectUtils.isEmpty(list)){
            //循环多个用户信息
            for (int i = 0; i < list.size(); i++){
                WechatUserParams userParams = list.get(i);
                //获取用户的openId
                String openId = wechatUserInfoService.getOpenId(wechatKfaccountTextMsgParams.getForeignSystemId(), wechatKfaccountTextMsgParams.getForeignSystem(), userParams.getForeignId(), userParams.getForeignType());
                //未获取到openId
                if (StringUtils.isEmpty(openId)){
                    this.saveFailedMessage(wechatKfaccountTextMsgParams, userParams, MobileHttpCode.HTTP_NOT_GET_WECHAT_OPEN_ID, "未获取到openId");
                }else{ //已经获取到openId
                    this.sendMessage(wechatKfaccountTextMsgParams, wechatAccount, openId, userParams, false);
                }
            }
        }
    }


    /**
     * 保存发送失败的消息
     */
    private void saveFailedMessage(WechatKfaccountTextMsgParams srcParams, WechatUserParams userParams, Integer errCode, String errorMsg){
        WechatKfaccountTextMsgParams tarParams = new WechatKfaccountTextMsgParams();
        BeanUtils.copyProperties(srcParams, tarParams);
        tarParams.setForeignId(userParams.getForeignId());
        tarParams.setForeignType(userParams.getForeignType());
        tarParams.setSendType(WechatConstants.SEND_SINGLE);
        //保存失败记录
        wechatKfaccountTextMsgFailedService.saveWechatKfaccountTextMsgFailed(JsonUtils.bean2Json(tarParams),  errCode, errorMsg, WechatConstants.MAX_RETRY_COUNT, WechatConstants.CURRENT_RETRY_INIT_COUNT);
    }


    /**
     * 向单个用户发送微信消息
     */
    private void sendSingleWechatKfaccountTextMsg(WechatKfaccountTextMsgParams wechatKfaccountTextMsgParams, WechatAccount wechatAccount) {
        String openId = "";
        if(StringUtils.isEmpty(wechatKfaccountTextMsgParams.getOpenId())){
            openId = wechatUserInfoService.getOpenId(wechatKfaccountTextMsgParams.getForeignSystemId(), wechatKfaccountTextMsgParams.getForeignSystem(), wechatKfaccountTextMsgParams.getForeignId(), wechatKfaccountTextMsgParams.getForeignType());
        }else{
            openId = wechatKfaccountTextMsgParams.getOpenId();
            //String foreignSystemId, @Param("foreignSystem") String foreignSystem, @Param("openId") String openId, @Param("foreignType") String foreignType
            String id = wechatUserInfoMapper.getId(wechatKfaccountTextMsgParams.getForeignSystemId(), wechatKfaccountTextMsgParams.getForeignSystem(), wechatKfaccountTextMsgParams.getOpenId(), wechatKfaccountTextMsgParams.getForeignType());
           //数据为空,保存数据
            if(StringUtils.isEmpty(id)){
                WechatUserInfo wechatUserInfo = new WechatUserInfo();
                wechatUserInfo.setForeignSystemId(wechatKfaccountTextMsgParams.getForeignSystemId());
                wechatUserInfo.setForeignSystem(wechatKfaccountTextMsgParams.getForeignSystem());
                wechatUserInfo.setSlaveUser(wechatAccount.getSlaveUser());
                wechatUserInfo.setOpenId(openId);
                wechatUserInfo.setForeignId(wechatKfaccountTextMsgParams.getForeignId());
                wechatUserInfo.setForeignType(wechatKfaccountTextMsgParams.getForeignType());
                wechatUserInfo.setNickname("");
                wechatUserInfo.setSex(0);
                wechatUserInfo.setProvince("");
                wechatUserInfo.setCity("");
                wechatUserInfo.setCountry("");
                wechatUserInfo.setHeadimgurl("");
                wechatUserInfo.setPrivilege("");
                wechatUserInfo.setUnionid("");
                wechatUserInfoMapper.saveWechatUserInfo(wechatUserInfo);
            }else{
                wechatUserInfoMapper.updateForeignId(wechatKfaccountTextMsgParams.getForeignId(), id) ;
            }
        }
        if(StringUtils.isEmpty(openId)){
            logger.info("未获取到微信openid....");
            throw new MyException("未获取到微信openid", MobileHttpCode.HTTP_NOT_GET_WECHAT_OPEN_ID);
        }
        this.sendMessage(wechatKfaccountTextMsgParams, wechatAccount, openId, null,true);
    }

    /**
     *  发送消息
     */
    private void sendMessage(WechatKfaccountTextMsgParams wechatKfaccountTextMsgParams, WechatAccount wechatAccount, String openId, WechatUserParams userParams, boolean isThrowException) {
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
            if (isThrowException){
                throw new MyException("服务端异常", MobileHttpCode.HTTP_SERVER_EXCEPTION);
            }else{
                //保存失败记录
                this.saveFailedMessage(wechatKfaccountTextMsgParams, userParams, MobileHttpCode.HTTP_SERVER_EXCEPTION, "服务端异常");
                return;
            }
        }
        if(StringUtils.isEmpty(ret)){
            if (isThrowException){
                throw new MyException("发送微信客服消息失败", MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED);
            }else{
                //保存失败记录
                this.saveFailedMessage(wechatKfaccountTextMsgParams, userParams, MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED, "发送微信客服消息失败");
                return;
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(ret);
        if (!jsonObject.containsKey(WechatConstants.WEHCAT_ERROR_CODE)){
            if (isThrowException){
                throw new MyException("发送微信客服消息失败", MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED);
            }else{
                //保存失败记录
                this.saveFailedMessage(wechatKfaccountTextMsgParams, userParams, MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED, "发送微信客服消息失败");
                return;
            }
        }
        Integer wechatCode = jsonObject.getInteger(WechatConstants.WEHCAT_ERROR_CODE);
        if(wechatCode == null || wechatCode != WechatConstants.WECHAT_CODE_NORMAL){
            if (isThrowException){
                throw new MyException("发送微信客服消息失败", MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED);
            }else{
                //保存失败记录
                this.saveFailedMessage(wechatKfaccountTextMsgParams, userParams, MobileHttpCode.HTTP_NOT_GET_WECHAT_TEMPLATE_SEND_FAILED, "发送微信客服消息失败");
                return;
            }
        }
        //保存记录
        WechatKfaccountTextMsgLog wechatKfaccountTextMsgLog = new WechatKfaccountTextMsgLog();
        wechatKfaccountTextMsgLog.setParameter(JsonUtils.bean2Json(wechatKfaccountTextMsgParams));
        wechatKfaccountTextMsgLog.setWxParameter(JsonUtils.bean2Json(wxKfaccountTextMessage));
        wechatKfaccountTextMsgLog.setResult(ret);
        wechatKfaccountTextMsgLog.setAccountId(wechatAccount.getId());
        wechatKfaccountTextMsgLog.setOpenId(openId);
        wechatKfaccountTextMsgLog.setRetry(wechatKfaccountTextMsgParams.getRetry());
        wechatKfaccountTextMsgLogMapper.saveWechatKfaccountTextMsgLog(wechatKfaccountTextMsgLog);
    }
}
