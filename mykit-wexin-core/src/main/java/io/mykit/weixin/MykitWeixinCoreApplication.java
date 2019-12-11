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
package io.mykit.weixin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author liuyazhuang
 * @date 2018/10/8 10:16
 * @description 工程启动类
 * @version 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"io.mykit.weixin" })
@MapperScan(value = {"io.mykit.weixin.mapper"})
@EnableTransactionManagement
@EnableScheduling
public class MykitWeixinCoreApplication {

    public static void main(String[] args){
        SpringApplication.run(MykitWeixinCoreApplication.class, args);
    }
}
