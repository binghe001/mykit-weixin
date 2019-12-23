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

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.mykit.wechat.cache.redis.RedisUtils;
import io.mykit.wechat.mp.beans.xml.receive.event.WxSubscribeEventMessage;
import io.mykit.wechat.mp.beans.xml.receive.event.WxUnSubscribeEventMessage;
import io.mykit.wechat.mp.beans.xml.send.text.WxSendTextMessage;
import io.mykit.wechat.mp.http.base.HttpConnectionUtils;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.constants.WxConstants;
import io.mykit.wechat.utils.xml.handler.XStreamHandler;
import io.mykit.weixin.config.LoadProp;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.entity.WechatUserSubscribe;
import io.mykit.weixin.mapper.WechatUserSubscribeMapper;
import io.mykit.weixin.service.WechatUserSubscribeService;
import io.mykit.weixin.utils.sign.sl.Sign;
import io.mykit.weixin.vo.WechatQrcodeSubscribeInfo;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuyazhuang
 * @date 2018/10/29 15:55
 * @description 微信用户关注记录Service实现类
 * @version 1.0.0
 */
@Service
public class WechatUserSubscribeServiceImpl implements WechatUserSubscribeService {
    private final Logger logger = LoggerFactory.getLogger(WechatUserSubscribeServiceImpl.class);
    @Resource
    private WechatUserSubscribeMapper wechatUserSubscribeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdateWxSubscribeEventMessage(WxSubscribeEventMessage wxSubscribeEventMessage) {
        int count = 0;
        //获取唯一的一条记录
        WechatUserSubscribe wechatUserSubscribe = wechatUserSubscribeMapper.getWechatUserSubscribe(wxSubscribeEventMessage.getToUserName(), wxSubscribeEventMessage.getFromUserName());
        if(wechatUserSubscribe == null || StringUtils.isEmpty(wechatUserSubscribe.getId())){
            wechatUserSubscribe = new WechatUserSubscribe();
            wechatUserSubscribe.setEvent(wxSubscribeEventMessage.getEvent());
            wechatUserSubscribe.setMsgType(wxSubscribeEventMessage.getMsgType());
            wechatUserSubscribe.setOpenId(wxSubscribeEventMessage.getFromUserName());
            wechatUserSubscribe.setSlaveUser(wxSubscribeEventMessage.getToUserName());
            count = wechatUserSubscribeMapper.saveWechatUserSubscribe(wechatUserSubscribe);
            logger.debug("保存用户关注的记录信息参数：" + wxSubscribeEventMessage.toJsonString(wxSubscribeEventMessage) + ", 执行结果: " + count);
        }else{
            //类型不相同，处理相关逻辑，类型相同，忽略
            if (!wechatUserSubscribe.getEvent().equals(wxSubscribeEventMessage.getEvent()) && (WechatConstants.WEHCAT_SUBSCRIBE.equals(wxSubscribeEventMessage.getEvent()) || WechatConstants.WEHCAT_UNSUBSCRIBE.equals(wxSubscribeEventMessage.getEvent()))){
                count = wechatUserSubscribeMapper.updateEventStatus(wxSubscribeEventMessage.getEvent(), wxSubscribeEventMessage.getToUserName(), wxSubscribeEventMessage.getFromUserName());
                logger.debug("更新数据库event状态信息，结果为: " + count);
            }else{
                logger.debug("事件通知的event类型和数据库中的event类型相同，不做任何处理");
            }
        }
        return count;
    }

    @Override
    public int updateWxUnSubscribeEventMessage(WxUnSubscribeEventMessage wxUnSubscribeEventMessage) {
        int count = 0;
        //获取唯一的一条记录
        WechatUserSubscribe wechatUserSubscribe = wechatUserSubscribeMapper.getWechatUserSubscribe(wxUnSubscribeEventMessage.getToUserName(), wxUnSubscribeEventMessage.getFromUserName());
        //记录不为空，同时事件状态不一致，则更新事件状态
        if(wechatUserSubscribe != null && !wxUnSubscribeEventMessage.getEvent().equals(wechatUserSubscribe.getEvent()) && (WechatConstants.WEHCAT_SUBSCRIBE.equals(wxUnSubscribeEventMessage.getEvent()) || WechatConstants.WEHCAT_UNSUBSCRIBE.equals(wxUnSubscribeEventMessage.getEvent()))){
            count = wechatUserSubscribeMapper.updateEventStatus(wxUnSubscribeEventMessage.getEvent(), wxUnSubscribeEventMessage.getToUserName(), wxUnSubscribeEventMessage.getFromUserName());
        }
        return count;
    }

    @Override
    public WechatUserSubscribe getWechatUserSubscribe(String slaveUser, String openId) {
        return wechatUserSubscribeMapper.getWechatUserSubscribe(slaveUser, openId);
    }

    @Override
    public PageInfo<WechatUserSubscribe> queryPage(Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        List<WechatUserSubscribe> list = wechatUserSubscribeMapper.getWechatUserSubscribes(status);
        return new PageInfo<WechatUserSubscribe>(list);
    }

    /**
     * 被动响应文本消息
     * @param toUserName：微信用户的openId
     * @param fromUserName: 开发者微信号
     * @param content：回复的文本消息
     * @return 返回的字符串
     */
    @Override
    public String getTextResponseResult(String toUserName, String fromUserName, String content){
        WxSendTextMessage wxSendTextMessage = new WxSendTextMessage();
        wxSendTextMessage.setContent(content);
        wxSendTextMessage.setMsgType(WxConstants.TYPE_TEXT);
        wxSendTextMessage.setCreateTime(System.currentTimeMillis());
        wxSendTextMessage.setFromUserName(fromUserName);
        wxSendTextMessage.setToUserName(toUserName);
        return XStreamHandler.toXml(wxSendTextMessage);
    }

    @Override
    public String getQrcodeSubscribeInfo(String openId) {
        return RedisUtils.getValueFromRedis(openId);
    }

    /**
     * 获取微信关注后的响应信息
     * @param openId 微信用户id
     * @param eventKey 扫码关注获取的eventKey
     * @return 关注后向公众号响应的消息
     */
    @Override
    public String getQrcodeSubscribeResponseMessage(String openId, String eventKey) {
        String response = "";
        try {
            Boolean flag = LoadProp.getBooleanValue(LoadProp.WEXIN_QRCODE_GET_DATA);
            if (flag){
                String result = this.getBusinessDataFromBusinessSystem(eventKey);
                String message = this.getStringDataFromBuinessResponse(result);
                //数据为空，则回复默认的消息
                if(StringUtils.isEmpty(message)){
                    //回复默认的数据
                    logger.info("message数据为空");
                    response = WechatConstants.getSubscribeResponseMessage();
                }else{      //数据不为空
                    WechatQrcodeSubscribeInfo subscribeInfo = this.getWechatQrcodeSubscribeInfoDataFromBuinessMessageResult(message);
                    if(subscribeInfo == null){
                        logger.info("subscribeInfo为空");
                        response = WechatConstants.getSubscribeResponseMessage();
                    }else{
                        response = WechatConstants.getQrcodeSubscribeResponseMessage(subscribeInfo, LoadProp.getStringValue(LoadProp.WEIXIN_QRCODE_LOAD_PAGE), openId);
                        //TODO 将解析出的message数据保存到缓存中,格式为openId:message
                        RedisUtils.saveValueToRedis(openId, message);
                    }
                }
            }else{   //设置为不请求业务系统，则返回默认的关注回复数据
                logger.info("不向业务系统获取数据");
                response = WechatConstants.getSubscribeResponseMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("数据异常，异常信息为===>>>" + e.getMessage());
            response = WechatConstants.getSubscribeResponseMessage();
        }
        return response;
    }

    /**
     * 获取eventKey里的内容
     * @param eventKey 微信传递过来的eventKey
     * @return 想要的数据
     */
    @Override
    public String getInfoFromEventKey(String eventKey){
        if (StringUtils.isEmpty(eventKey)){
            return "";
        }
        if(eventKey.contains(WechatConstants.QRSCENE_PREFIX)){
            eventKey = eventKey.substring(WechatConstants.QRSCENE_PREFIX.length());
        }
        return eventKey;
    }


    /**
     * 获取业务系统中的数据
     */
    private String getBusinessDataFromBusinessSystem(String docId) throws Exception{
        String url = LoadProp.getStringValue(LoadProp.WEIXIN_QRCODE_SUBSCRIBE_URL);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("docId", docId);
        Sign sign = new Sign();
        NameValuePair[] nameValuePairs = new NameValuePair[]{
                new NameValuePair("nonce_str", sign.getNonce_str()),
                new NameValuePair("company", sign.getCompany()),
                new NameValuePair("timeStamp", sign.getTimeStamp()),
                new NameValuePair("sign", sign.getSign()),
                new NameValuePair("server", "fd")
        };
        String result = HttpConnectionUtils.postDataWithParameter(url, jsonObject.toJSONString(), nameValuePairs, null, HttpConnectionUtils.TYPE_STREAM);
        return result;
    }

    /**
     * 获取业务系统中的响应数据
     * @param response 业务系统返回的完整数据
     * @return JSONObject对象
     */
    private JSONObject getJSONObjectDataFromBuinessResponse(String response){
        JSONObject jsonObject = JSONObject.parseObject(response);
        if(jsonObject == null || jsonObject.size() == 0){
            return null;
        }
        if (!jsonObject.containsKey(WechatConstants.CODE)){
            return null;
        }
        Integer code = jsonObject.getInteger(WechatConstants.CODE);
        if(WechatConstants.CODE_SUCCESS.compareTo(code) != 0){
            return null;
        }
        return jsonObject.getJSONObject(WechatConstants.MESSAGE);
    }

    /**
     * 获取业务系统中的响应数据
     * @param response 业务系统返回的完整数据
     * @return JSON 字符串
     */
    private String getStringDataFromBuinessResponse(String response){
        JSONObject jsonObject = this.getJSONObjectDataFromBuinessResponse(response);
        if (jsonObject == null) return "";
        return jsonObject.toJSONString();
    }
    public static void main(String[] args){
        String str = "{\"message\":{\"secId\":\"123fsdfdsdfewar54353453425sdfse4\",\"id\":\"3983758347587854\",\"docName\":\"医生助手\",\"hospName\":\"关心堂健康管理中心\",\"status\":1,\"secName\":\"内科\",\"hospId\":\"8a8383af60747eb8016096fdb3dd0144\",\"docId\":\"8a8383a85cd483f3015ce78ba9f4005f\",\"docAccount\":\"15928819479\",\"teamId\":\"8a8383a76dc990f3016ece350bc80522\"},\"sign\":{\"sign\":\"15677A7260CA987FF84483E8E965E137\",\"timeStamp\":\"1577082761709\",\"company\":\"6F79078F79D77550739EF61CD0DC2A83\",\"nonce_str\":\"cj716q3ka61l9cocm0rpozk7afd5bg43\"},\"code\":1001}";
        WechatUserSubscribeServiceImpl impl = new WechatUserSubscribeServiceImpl();
        String result = impl.getStringDataFromBuinessResponse(str);
        System.out.println(result);
    }


    /**
     * 获取业务系统中的响应数据
     * @param response 业务系统返回的完整数据
     * @return WechatQrcodeSubscribeInfo对象
     */
    private WechatQrcodeSubscribeInfo getWechatQrcodeSubscribeInfoDataFromBuinessResponse(String response){
        JSONObject jsonObject = this.getJSONObjectDataFromBuinessResponse(response);
        if(jsonObject == null) return null;
        return jsonObject.toJavaObject(WechatQrcodeSubscribeInfo.class);
    }

    /**
     * 获取业务系统中的响应数据
     * @param messageResult 业务系统返回的完整数据中解析出的message字段数据
     * @return WechatQrcodeSubscribeInfo对象
     */
    private WechatQrcodeSubscribeInfo getWechatQrcodeSubscribeInfoDataFromBuinessMessageResult(String messageResult){
        JSONObject jsonObject = JSONObject.parseObject(messageResult);
        if(jsonObject == null) return null;
        return jsonObject.toJavaObject(WechatQrcodeSubscribeInfo.class);
    }
}
