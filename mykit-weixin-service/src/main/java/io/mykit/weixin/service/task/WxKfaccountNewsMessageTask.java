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
package io.mykit.weixin.service.task;

import io.mykit.wechat.mp.beans.json.kfaccount.message.news.WxKfaccountNewsArticlesItemsMessage;
import io.mykit.wechat.mp.beans.json.kfaccount.message.news.WxKfaccountNewsArticlesMessage;
import io.mykit.wechat.mp.beans.json.kfaccount.message.news.WxKfaccountNewsMessage;
import io.mykit.wechat.mp.http.handler.kfaccount.WxKfaccountHandler;
import io.mykit.wechat.utils.common.ObjectUtils;
import io.mykit.weixin.entity.WechatAccount;
import io.mykit.weixin.mapper.WechatUserInfoMapper;
import io.mykit.weixin.params.WechatKfaccountNewsMsgParams;
import io.mykit.weixin.utils.page.support.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author binghe
 * @version 1.0.0
 * @description 发送图文消息的任务类
 */
public class WxKfaccountNewsMessageTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(WxKfaccountNewsMessageTask.class);

    private WechatUserInfoMapper wechatUserInfoMapper;
    private WechatKfaccountNewsMsgParams wechatKfaccountNewsMsgParams;
    private WechatAccount wechatAccount;

    public WxKfaccountNewsMessageTask(WechatUserInfoMapper wechatUserInfoMapper, WechatKfaccountNewsMsgParams wechatKfaccountNewsMsgParams, WechatAccount wechatAccount) {
        this.wechatUserInfoMapper = wechatUserInfoMapper;
        this.wechatKfaccountNewsMsgParams = wechatKfaccountNewsMsgParams;
        this.wechatAccount = wechatAccount;
    }

    @Override
    public void run() {
        WxKfaccountNewsMessage wxKfaccountNewsMessage = this.getWxKfaccountNewsMessage(wechatKfaccountNewsMsgParams);
        int page = 1;
        int pageSize = 100;
        BasePage<String> basePage = this.queryPage(pageSize, page);
        this.sendNewsMessage(basePage.getList(), wxKfaccountNewsMessage);
        //存在下一页
        while (basePage.isHasNextPage()){
            page ++;
            basePage = this.queryPage(pageSize, page);
            this.sendNewsMessage(basePage.getList(), wxKfaccountNewsMessage);
        }

    }

    private void sendNewsMessage(List<String> openIds,  WxKfaccountNewsMessage wxKfaccountNewsMessage){
        if(!ObjectUtils.isEmpty(openIds)){
            for(String openId : openIds){
                wxKfaccountNewsMessage.setTouser(openId);
                this.sendNewsMessage(wxKfaccountNewsMessage);
            }
        }
    }

    private BasePage<String> queryPage(int pageSize, int page){
        //获取总数量
        int allRow = wechatUserInfoMapper.getCount(wechatKfaccountNewsMsgParams.getForeignSystemId(), wechatKfaccountNewsMsgParams.getForeignSystem());
        // 计算总页数
        int totalPage = BasePage.countTotalPage(pageSize, allRow);
        // 当前开始的记录
        int pageStartR = BasePage.countOffset(pageSize, page);
        if (pageStartR < 0)
            pageStartR = 0;
        // 当前页开始记录
        int offset = pageStartR;
        // 每页的记录数
        final int length = pageSize;
        // 当前页码
        final int currentPage = BasePage.countCurrentPage(page);
        //获取的列表
        List<String> openIds = wechatUserInfoMapper.getOpenIdPageList(offset, length, wechatKfaccountNewsMsgParams.getForeignSystemId(), wechatKfaccountNewsMsgParams.getForeignSystem());
        return new BasePage<String>(allRow, totalPage, currentPage, pageSize, openIds);
    }

    /**
     * 构造图文消息
     */
    private WxKfaccountNewsMessage getWxKfaccountNewsMessage(WechatKfaccountNewsMsgParams wechatKfaccountNewsMsgParams){
        WxKfaccountNewsArticlesItemsMessage wxKfaccountNewsArticlesItemsMessage = new WxKfaccountNewsArticlesItemsMessage();
        wxKfaccountNewsArticlesItemsMessage.setDescription(wechatKfaccountNewsMsgParams.getDescription());
        wxKfaccountNewsArticlesItemsMessage.setPicurl(wechatKfaccountNewsMsgParams.getPicUrl());
        wxKfaccountNewsArticlesItemsMessage.setTitle(wechatKfaccountNewsMsgParams.getTitle());
        wxKfaccountNewsArticlesItemsMessage.setUrl(wechatKfaccountNewsMsgParams.getUrl());

        WxKfaccountNewsArticlesMessage wxKfaccountNewsArticlesMessage = new WxKfaccountNewsArticlesMessage();
        wxKfaccountNewsArticlesMessage.setArticles(Arrays.asList(wxKfaccountNewsArticlesItemsMessage));

        WxKfaccountNewsMessage wxKfaccountNewsMessage = new WxKfaccountNewsMessage();
        wxKfaccountNewsMessage.setNews(wxKfaccountNewsArticlesMessage);
        wxKfaccountNewsMessage.setMsgtype("news");
        return wxKfaccountNewsMessage;
    }

    /**
     * 发送图文消息
     */
    private void sendNewsMessage(WxKfaccountNewsMessage wxKfaccountNewsMessage){
        try {
            String result = WxKfaccountHandler.sendWxKfaccountNewsMessage(wechatAccount.getAppId(), wechatAccount.getAppSecret(), wxKfaccountNewsMessage);
            logger.info(result);
        } catch (Exception e) {
            logger.error("全员发送微信客服图文消息异常：{}", e);
        }
    }
}
