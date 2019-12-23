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
package io.mykit.weixin.config;

import io.mykit.wechat.utils.common.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author binghe
 * @version 1.0.0
 * @description 加载文件
 */
public class LoadProp extends BaseProp {

    private volatile static Properties mProperties;

    static{
        mProperties = new Properties();
        InputStream in = LoadProp.class.getClassLoader().getResourceAsStream("properties/params.properties");
        try {
            mProperties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStringValue(String key){
        return mProperties.getProperty(key, "");
    }

    public static Integer getIntValue(String key){
        return Integer.parseInt(mProperties.getProperty(key, ""));
    }

    public static Boolean getBooleanValue(String key){
        String value = getStringValue(key);
        return StringUtils.isEmpty(value) ? false : Boolean.parseBoolean(value);
    }
}
