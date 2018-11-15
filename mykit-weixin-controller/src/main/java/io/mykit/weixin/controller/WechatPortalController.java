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
package io.mykit.weixin.controller;

import io.mykit.wechat.mp.beans.router.receive.WxReceiveRouterResult;
import io.mykit.wechat.mp.beans.xml.receive.event.WxSubscribeEventMessage;
import io.mykit.wechat.mp.beans.xml.receive.event.WxUnSubscribeEventMessage;
import io.mykit.wechat.mp.core.router.WxReceiveMessageRouter;
import io.mykit.wechat.utils.constants.WxConstants;
import io.mykit.wechat.utils.sign.WechatSignUtils;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.service.WechatUserSubscribeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author liuyazhuang
 * @date 2018/10/29 10:29
 * @description 微信回调业务入口，目前，多公众号只支持明文传输
 * @version 1.0.0
 */
@RestController
@RequestMapping("/wechat/portal")
public class WechatPortalController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WechatUserSubscribeService wechatUserSubscribeService;

    /**
     * 验证签名，确认消息时从微信服务器接收到的
     * @param signature 微信服务器发送过来的签名
     * @param timestamp 微信服务器发送过来的时间戳
     * @param nonce 微信服务器发送过来的随机数
     * @param echostr 微信服务器发送过来的数据
     * @return 响应结果
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(
            @RequestParam(name = "signature",
                    required = false) String signature,
            @RequestParam(name = "timestamp",
                    required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr) {
        this.logger.debug("接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,  timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        if (WechatSignUtils.checkSignature(WechatConstants.TOKEN, timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    /**
     * 接收微信服务器发送过来的数据
     * @param requestBody 请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param encType 加密类型
     * @param msgSignature 消息签名
     * @return 响应结果
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam(name = "encrypt_type",
                               required = false) String encType,
                       @RequestParam(name = "msg_signature",
                               required = false) String msgSignature) {

        if (!WechatSignUtils.checkSignature(WechatConstants.TOKEN, timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        //处理业务逻辑
        return this.routeResult(WxReceiveMessageRouter.router(requestBody));

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
            default:
        }
        return "";
    }


    /**
     * 路由关注事件
     * @param wxReceiveRouterResult 收到的微信消息
     * @return 返回结果
     */
    private String routerEventSubscribe(WxReceiveRouterResult wxReceiveRouterResult) {
        WxSubscribeEventMessage wxSubscribeEventMessage = (WxSubscribeEventMessage) wxReceiveRouterResult.getBaseReceiveMessage();
        wechatUserSubscribeService.saveOrUpdateWxSubscribeEventMessage(wxSubscribeEventMessage);
        return "";
    }

    /**
     * 路由取消关注事件
     * @param wxReceiveRouterResult 收到的微信消息
     * @return 返回结果
     */
    private String routerEventUnSubscribe(WxReceiveRouterResult wxReceiveRouterResult) {
        WxUnSubscribeEventMessage wxUnSubscribeEventMessage = (WxUnSubscribeEventMessage) wxReceiveRouterResult.getBaseReceiveMessage();
        wechatUserSubscribeService.updateWxUnSubscribeEventMessage(wxUnSubscribeEventMessage);
        return "";
    }
}
