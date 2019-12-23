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
package io.mykit.weixin.test.string;

import io.mykit.weixin.config.LoadProp;
import io.mykit.weixin.constants.wechat.WechatConstants;
import io.mykit.weixin.test.http.HttpTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author binghe
 * @version 1.0.0
 * @description 测试字符串
 */
public class StringTest {
    private final Logger logger = LoggerFactory.getLogger(HttpTest.class);

    @Test
    public void testSubString(){
        String str = "qrscene_8a8383a85cd483f3015ce78ba9f4005f";
        String subStr = str.substring(WechatConstants.QRSCENE_PREFIX.length());
        logger.info(subStr);
        //8a8383a85cd483f3015ce78ba9f4005f
        //8a8383a85cd483f3015ce78ba9f4005f
    }

    @Test
    public void testBooleanData(){
        Boolean flag = LoadProp.getBooleanValue(LoadProp.WEXIN_QRCODE_GET_DATA);
        if (flag){
            logger.info("获取到了");
        }
    }
}
