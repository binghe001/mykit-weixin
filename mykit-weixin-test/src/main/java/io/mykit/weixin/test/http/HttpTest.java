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
package io.mykit.weixin.test.http;

import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.mp.http.base.HttpConnectionUtils;
import io.mykit.weixin.config.LoadProp;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.utils.sign.sl.Sign;
import io.mykit.weixin.vo.WechatQrcodeSubscribeInfo;
import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author binghe
 * @version 1.0.0
 * @description 测试http访问
 */
public class HttpTest {

    private final Logger logger = LoggerFactory.getLogger(HttpTest.class);

    @Test
    public void testPost() throws Exception{
        String result =  getHttpResult();
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject message = jsonObject.getJSONObject("message");
        WechatQrcodeSubscribeInfo wechatQrcodeSubscribeInfo = message.toJavaObject(WechatQrcodeSubscribeInfo.class);
        String page = WechatConstants.getQrcodeSubscribeResponseMessage(wechatQrcodeSubscribeInfo, LoadProp.getStringValue(LoadProp.WEIXIN_QRCODE_LOAD_PAGE), "123456");
        logger.info(page);
    }

    private String getHttpResult() throws Exception {
        String url = LoadProp.getStringValue(LoadProp.WEIXIN_QRCODE_SUBSCRIBE_URL);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("docId", "8a8383a85cd483f3015ce78ba9f4005f");
        Sign sign = new Sign();
        NameValuePair[] nameValuePairs = new NameValuePair[]{
                new NameValuePair("nonce_str", sign.getNonce_str()),
                new NameValuePair("company", sign.getCompany()),
                new NameValuePair("timeStamp", sign.getTimeStamp()),
                new NameValuePair("sign", sign.getSign()),
                new NameValuePair("server", "fd")
        };
        //String url, String body, NameValuePair[] nameValuePairs, Map<String, String> headers, String type
        String ret = HttpConnectionUtils.postDataWithParameter(url, jsonObject.toJSONString(), nameValuePairs, null, HttpConnectionUtils.TYPE_STREAM);
        logger.info(ret);
        return ret;
    }
}
