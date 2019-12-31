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

import io.mykit.wechat.mp.beans.json.factory.json.QrcodeParamsFactory;
import io.mykit.wechat.mp.beans.json.qrcode.WxQrcodeForever;
import io.mykit.wechat.mp.beans.json.qrcode.WxQrcodeResp;
import io.mykit.wechat.mp.beans.json.qrcode.WxQrcodeTemporary;
import io.mykit.wechat.mp.http.handler.qrcode.WxQrcodeHandler;
import io.mykit.wechat.utils.common.DateUtils;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.entity.WechatQrcode;
import io.mykit.weixin.mapper.WechatQrcodeMapper;
import io.mykit.weixin.params.WechatQrcodeParams;
import io.mykit.weixin.service.WechatAccountService;
import io.mykit.weixin.service.WechatQrcodeServcie;
import io.mykit.weixin.service.impl.base.WechatCacheServiceImpl;
import io.mykit.weixin.utils.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

/**
 * @author binghe
 * @version 1.0.0
 * @description 微信二维码业务
 */
@Service
public class WechatQrcodeServiceImpl extends WechatCacheServiceImpl implements WechatQrcodeServcie {
    private final Logger logger = LoggerFactory.getLogger(WechatQrcodeServiceImpl.class);
    @Resource
    private WechatQrcodeMapper wechatQrcodeMapper;
    @Resource
    private WechatAccountService wechatAccountService;

    @Override
    public void createAndSaveQrcode(WechatQrcodeParams wechatQrcodeParams) {
        WechatQrcode wechatQrcode = this.getNativeWechatQrcode(wechatQrcodeParams);
        if (wechatQrcode != null && !StringUtils.isEmpty(wechatQrcode.getId())){
            //二维码已经存在，不可重复生成
            logger.info("二维码已经存在，不可重复生成 ===>>> " + wechatQrcode.toJsonString());
            throw new MyException("二维码已经存在，不可重复生成", MobileHttpCode.HTTP_WECHAT_QRCODE_EXISTS);
        }
        WechatAccount wechatAccount = wechatAccountService.getWechatAccountByForeignIdAndSystem(wechatQrcodeParams.getForeignSystemId(), wechatQrcodeParams.getForeignSystem());
        if(wechatAccount == null){
            logger.info("未获取到微信开发者账号信息....");
            throw new MyException("未获取到微信开发者账号信息", MobileHttpCode.HTTP_NOT_GET_WECHAT_ACCOUNT);
        }
        //以foreignId为参数生成二维码
        WxQrcodeResp wxQrcodeResp = this.createQrcode(wechatAccount.getAppId(), wechatAccount.getAppSecret(), wechatQrcodeParams.getForeignId(), wechatQrcodeParams.getQrcodeType(), wechatQrcodeParams.getExpireSeconds());
        if(wxQrcodeResp == null){
            logger.info("生成微信二维码报错");
            throw new MyException("生成微信二维码报错", MobileHttpCode.HTTP_WECHAT_CREATE_QRCODE_ERROR);
        }
        if (wxQrcodeResp.getErrcode().compareTo(WxQrcodeResp.ERRCODE_NORMAL) != 0){
            logger.info("生成微信二维码报错");
            throw new MyException("生成微信二维码报错", MobileHttpCode.HTTP_WECHAT_CREATE_QRCODE_ERROR);
        }
        //文件名称
        String fileName = wechatQrcodeParams.getForeignSystemId().concat(wechatQrcodeParams.getQrcodeType()).concat(wechatQrcodeParams.getForeignId()).concat(".").concat("jpg");
        //本地存储绝对路径
        String filePath = io.mykit.weixin.config.LoadProp.getStringValue(io.mykit.weixin.config.LoadProp.WEIXIN_SYSTEM_QRCODE_PREFIX_PATH).concat(fileName);
        //网络访问路径
        String fileUrl = io.mykit.weixin.config.LoadProp.getStringValue(io.mykit.weixin.config.LoadProp.WEIXIN_SYSTEM_QRCODE_PREFIX_URL).concat(fileName);
        //下载微信二维码图片
        File file = null;
        try {
             file = WxQrcodeHandler.downloadQrcode(filePath, wxQrcodeResp.getTicket());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("下载微信二维码失败===>>>" + wxQrcodeResp.toJsonString());
            throw new MyException("下载微信二维码失败", MobileHttpCode.HTTP_WECHAT_DOWNLOAD_QRCODE_ERROR);
        }
        if (file == null){
            logger.info("下载微信二维码失败===>>>" + wxQrcodeResp.toJsonString());
            throw new MyException("下载微信二维码失败", MobileHttpCode.HTTP_WECHAT_DOWNLOAD_QRCODE_ERROR);
        }
        //下载微信二维码成功，保存数据
        wechatQrcode = new WechatQrcode();
        wechatQrcode.setForeignSystemId(wechatQrcodeParams.getForeignSystemId());
        wechatQrcode.setForeignSystem(wechatQrcodeParams.getForeignSystem());
        wechatQrcode.setForeignId(wechatQrcodeParams.getForeignId());
        wechatQrcode.setForeignType(wechatQrcodeParams.getForeignType());
        wechatQrcode.setQrcodeInfo(wechatQrcodeParams.getForeignId());
        wechatQrcode.setTicket(wxQrcodeResp.getTicket());
        wechatQrcode.setWechatQrcodeUrl(wxQrcodeResp.getUrl());
        wechatQrcode.setQrcodeUrl(fileUrl);
        wechatQrcode.setQrcodePath(filePath);
        Long expireTime = System.currentTimeMillis() + wechatQrcodeParams.getExpireSeconds() * 1000;
        wechatQrcode.setExpireTime(expireTime);
        Date date = new Date(expireTime);
        wechatQrcode.setExpireTimeStr(DateUtils.parseDateToString(date, DateUtils.DATE_TIME_FORMAT));
        wechatQrcode.setQrcodeType(wechatQrcodeParams.getQrcodeType());
        wechatQrcodeMapper.saveWechatQrcode(wechatQrcode);
    }


    /**
     * 生成二维码
     * @param appid appid
     * @param appSecret  appsecret
     * @param foreignId 二维码参数
     * @param qrcodeType 永久二维码还是临时二维码
     * @return 微信返回结果
     */
    private WxQrcodeResp createQrcode(String appid, String appSecret, String foreignId, String qrcodeType, Integer expireSeconds){
       switch (qrcodeType){
           //临时二维码
           case WechatQrcode.TYPE_EXPIRE:
               return this.createExpireQrcode(appid, appSecret, foreignId, expireSeconds);
           //永久二维码
           case WechatQrcode.TYPE_NOEXPIRE:
               return this.createNoExpireQrcode(appid, appSecret, foreignId);
           default:
               return this.createNoExpireQrcode(appid, appSecret, foreignId);
       }
    }

    /**
     * 生成永久二维码
     * @param appid appid
     * @param appSecret  appsecret
     * @param foreignId 二维码参数
     * @return 微信返回结果
     */
    private WxQrcodeResp createNoExpireQrcode(String appid, String appSecret, String foreignId) {
        WxQrcodeForever wxQrcodeForever = QrcodeParamsFactory.getWxQrcodeForeverByStringParams(foreignId);
        WxQrcodeResp wxQrcodeResp = null;
        try{
            wxQrcodeResp = WxQrcodeHandler.createWxQrcodeForever(appid, appSecret, wxQrcodeForever);
        }catch (Exception e){
            logger.info("生成微信永久二维码报错：" + e.getMessage());
            throw new MyException("生成微信永久二维码报错", MobileHttpCode.HTTP_WECHAT_CREATE_QRCODE_ERROR);
        }
        return wxQrcodeResp;
    }

    /**
     * 生成临时二维码
     * @param appid appid
     * @param appSecret  appsecret
     * @param foreignId 二维码参数
     * @return 微信返回结果
     */
    private WxQrcodeResp createExpireQrcode(String appid, String appSecret, String foreignId,  Integer expireSeconds) {
        WxQrcodeTemporary wxQrcodeTemporary = QrcodeParamsFactory.getWxQrcodeTemporaryByStringParams(foreignId, expireSeconds);
        WxQrcodeResp wxQrcodeResp = null;
        try {
            wxQrcodeResp = WxQrcodeHandler.createWxQrcodeTemporary(appid, appSecret, wxQrcodeTemporary);
        } catch (Exception e) {
            logger.info("生成微信临时二维码报错：" + e.getMessage());
            throw new MyException("生成微信临时二维码报错", MobileHttpCode.HTTP_WECHAT_CREATE_QRCODE_ERROR);
        }
        return wxQrcodeResp;
    }

    @Override
    public WechatQrcode getWechatQrcode(WechatQrcodeParams wechatQrcodeParams) {
        WechatQrcode wechatQrcode = this.getNativeWechatQrcode(wechatQrcodeParams);
        if (wechatQrcode == null || StringUtils.isEmpty(wechatQrcode.getId())){
            logger.info("未生成二维码或二维码已失效");
            throw new MyException("未生成二维码或二维码已失效", MobileHttpCode.HTTP_WECHAT_QRCODE_NOT_EXISTS);
        }
        return wechatQrcode;
    }

    private WechatQrcode getNativeWechatQrcode(WechatQrcodeParams wechatQrcodeParams) {

        switch (wechatQrcodeParams.getQrcodeType()) {
            case WechatQrcode.TYPE_EXPIRE:
                return wechatQrcodeMapper.getExpireWechatQrcode(wechatQrcodeParams.getForeignSystemId(),
                        wechatQrcodeParams.getForeignSystem(),
                        wechatQrcodeParams.getForeignId(),
                        wechatQrcodeParams.getForeignType(),
                        wechatQrcodeParams.getQrcodeType(),
                        System.currentTimeMillis());
            case WechatQrcode.TYPE_NOEXPIRE:
                return wechatQrcodeMapper.getNoExpireWechatQrcode(wechatQrcodeParams.getForeignSystemId(),
                        wechatQrcodeParams.getForeignSystem(),
                        wechatQrcodeParams.getForeignId(),
                        wechatQrcodeParams.getForeignType(),
                        wechatQrcodeParams.getQrcodeType());
            default:
                return wechatQrcodeMapper.getNoExpireWechatQrcode(wechatQrcodeParams.getForeignSystemId(),
                        wechatQrcodeParams.getForeignSystem(),
                        wechatQrcodeParams.getForeignId(),
                        wechatQrcodeParams.getForeignType(),
                        wechatQrcodeParams.getQrcodeType());
        }
    }

}
