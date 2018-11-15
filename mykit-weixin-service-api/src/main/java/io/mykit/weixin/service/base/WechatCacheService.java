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
package io.mykit.weixin.service.base;

import redis.clients.jedis.JedisCluster;

/**
 * @author liuyazhuang
 * @date 2018/10/30 13:29
 * @description 微信基础Service
 * @version 1.0.0
 */
public interface WechatCacheService {

    /**
     * 获取缓存句柄
     * @return 缓存句柄
     */
    JedisCluster getJedisCluster();
}
