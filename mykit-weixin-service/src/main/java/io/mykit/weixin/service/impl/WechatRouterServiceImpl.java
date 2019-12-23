/**
 * Copyright 2020-9999 the original author or authors.
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

import io.mykit.wechat.mp.beans.router.receive.WxReceiveRouterResult;
import io.mykit.wechat.mp.beans.xml.receive.event.WxQrcodeScanEventMessage;
import io.mykit.wechat.mp.beans.xml.receive.event.WxQrcodeSubscribeEventMessage;
import io.mykit.wechat.mp.beans.xml.receive.event.WxSubscribeEventMessage;
import io.mykit.wechat.mp.beans.xml.receive.event.WxUnSubscribeEventMessage;
import io.mykit.wechat.mp.core.router.WxReceiveMessageRouter;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.wechat.utils.constants.WxConstants;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.service.WechatRouterService;
import io.mykit.weixin.service.WechatUserSubscribeService;
import io.mykit.weixin.service.impl.base.WechatCacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author binghe
 * @version 1.0.0
 * @description 微信路由服务实现类
 */
@Service
public class WechatRouterServiceImpl extends WechatCacheServiceImpl implements WechatRouterService {
    private final Logger logger = LoggerFactory.getLogger(WechatRouterServiceImpl.class);

    @Resource
    private WechatUserSubscribeService wechatUserSubscribeService;

    @Override
    @Transactional
    public String routeResult(String requestBody) {
        //文本消息为空
        if (StringUtils.isEmpty(requestBody)){
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        WxReceiveRouterResult router = WxReceiveMessageRouter.router(requestBody);
        return this.routeResult(router);
    }

    /**
     * 路由消息
     * @param wxReceiveRouterResult 封装的微信结果信息
     * @return 结果信息
     */
    private String routeResult(WxReceiveRouterResult wxReceiveRouterResult) {
        switch (wxReceiveRouterResult.getRouterType()){
            //关注事件
            case WxConstants.ROUTER_EVENT_SUBSCRIBE:
                return routerEventSubscribe(wxReceiveRouterResult);
            //取消关注事件
            case WxConstants.ROUTER_EVENT_UNSUBSCRIBE:
                return routerEventUnSubscribe(wxReceiveRouterResult);
            //扫码关注
            case WxConstants.ROUTER_EVENT_QRCODE_SUBSCRIBE:
                return routerQrcodeEventSubscribe(wxReceiveRouterResult);
            //已经关注再次扫码
            case WxConstants.ROUTER_EVENT_SCAN:
                return routerEventScan(wxReceiveRouterResult);
            default:
        }
        return "";
    }


    /**
     * 扫描二维码，用户已关注时的类型
     * @param wxReceiveRouterResult  收到的微信消息
     * @return 返回结果
     */
    private String routerEventScan(WxReceiveRouterResult wxReceiveRouterResult){
        WxQrcodeScanEventMessage wxQrcodeScanEventMessage = (WxQrcodeScanEventMessage)wxReceiveRouterResult.getBaseReceiveMessage();
        WxSubscribeEventMessage wxSubscribeEventMessage = new WxSubscribeEventMessage();
        wxSubscribeEventMessage.setCreateTime(wxQrcodeScanEventMessage.getCreateTime());
        wxSubscribeEventMessage.setEvent(wxQrcodeScanEventMessage.getEvent());
        wxSubscribeEventMessage.setFromUserName(wxQrcodeScanEventMessage.getFromUserName());
        wxSubscribeEventMessage.setMsgType(wxQrcodeScanEventMessage.getMsgType());
        wxSubscribeEventMessage.setToUserName(wxQrcodeScanEventMessage.getToUserName());
        wechatUserSubscribeService.saveOrUpdateWxSubscribeEventMessage(wxSubscribeEventMessage);
        //打印EventKey
        String eventKey = wechatUserSubscribeService.getInfoFromEventKey(wxQrcodeScanEventMessage.getEventKey());
        //logger.info("用户已关注再次扫码===>>>"+ eventKey);
        //TODO 保存二维码参数
        String response = wechatUserSubscribeService.getQrcodeSubscribeResponseMessage(wxQrcodeScanEventMessage.getFromUserName(), eventKey);
        return wechatUserSubscribeService.getTextResponseResult(wxQrcodeScanEventMessage.getFromUserName(), wxQrcodeScanEventMessage.getToUserName(), response);
    }

    /**
     * 扫描带参数二维码事件
     * @param wxReceiveRouterResult 收到的微信消息
     * @return 返回结果
     */
    private String routerQrcodeEventSubscribe(WxReceiveRouterResult wxReceiveRouterResult){
        WxQrcodeSubscribeEventMessage wxQrcodeSubscribeEventMessage = (WxQrcodeSubscribeEventMessage)wxReceiveRouterResult.getBaseReceiveMessage();
        WxSubscribeEventMessage wxSubscribeEventMessage = new WxSubscribeEventMessage();
        wxSubscribeEventMessage.setCreateTime(wxQrcodeSubscribeEventMessage.getCreateTime());
        wxSubscribeEventMessage.setEvent(wxQrcodeSubscribeEventMessage.getEvent());
        wxSubscribeEventMessage.setFromUserName(wxQrcodeSubscribeEventMessage.getFromUserName());
        wxSubscribeEventMessage.setMsgType(wxQrcodeSubscribeEventMessage.getMsgType());
        wxSubscribeEventMessage.setToUserName(wxQrcodeSubscribeEventMessage.getToUserName());
        wechatUserSubscribeService.saveOrUpdateWxSubscribeEventMessage(wxSubscribeEventMessage);
        String eventKey = wechatUserSubscribeService.getInfoFromEventKey(wxQrcodeSubscribeEventMessage.getEventKey());
        //打印EventKey
        //logger.info("扫描二维码关注===>>>" + eventKey);
        String response = wechatUserSubscribeService.getQrcodeSubscribeResponseMessage(wxQrcodeSubscribeEventMessage.getFromUserName(), eventKey);
        return wechatUserSubscribeService.getTextResponseResult(wxQrcodeSubscribeEventMessage.getFromUserName(), wxQrcodeSubscribeEventMessage.getToUserName(), response);
    }


    /**
     * 路由关注事件
     * @param wxReceiveRouterResult 收到的微信消息
     * @return 返回结果
     */
    private String routerEventSubscribe(WxReceiveRouterResult wxReceiveRouterResult) {
        WxSubscribeEventMessage wxSubscribeEventMessage = (WxSubscribeEventMessage) wxReceiveRouterResult.getBaseReceiveMessage();
        wechatUserSubscribeService.saveOrUpdateWxSubscribeEventMessage(wxSubscribeEventMessage);
        return wechatUserSubscribeService.getTextResponseResult(wxSubscribeEventMessage.getFromUserName(), wxSubscribeEventMessage.getToUserName(), WechatConstants.getSubscribeResponseMessage());
    }

    /**
     * 路由取消关注事件
     * @param wxReceiveRouterResult 收到的微信消息
     * @return 返回结果
     */
    private String routerEventUnSubscribe(WxReceiveRouterResult wxReceiveRouterResult) {
        WxUnSubscribeEventMessage wxUnSubscribeEventMessage = (WxUnSubscribeEventMessage) wxReceiveRouterResult.getBaseReceiveMessage();
        wechatUserSubscribeService.updateWxUnSubscribeEventMessage(wxUnSubscribeEventMessage);
        return wechatUserSubscribeService.getTextResponseResult(wxUnSubscribeEventMessage.getFromUserName(), wxUnSubscribeEventMessage.getToUserName(), WechatConstants.getSubscribeResponseMessage());
    }

}
