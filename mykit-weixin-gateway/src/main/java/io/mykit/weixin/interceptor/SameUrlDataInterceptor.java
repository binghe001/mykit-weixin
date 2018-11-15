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
package io.mykit.weixin.interceptor;

import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.interceptor.annotation.SameUrlData;
import io.mykit.weixin.utils.resp.helper.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author liuyazhuang
 * @date 2018/10/8 19:08
 * @description 防止重复提交拦截器
 * @version 1.0.0
 */
@Slf4j
public abstract class SameUrlDataInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug(SameUrlDataInterceptor.class.getName() + "====>>>preHandle");
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            SameUrlData annotation = method.getAnnotation(SameUrlData.class);
            if(annotation != null){
                Integer code = repeatDataValidator(request, response, annotation);
                if(code != MobileHttpCode.HTTP_NORMAL){
                    ResponseHelper.responseMessage(null, false, false, code, response);
                }
                return code == MobileHttpCode.HTTP_NORMAL;
            }
            return true;
        }else{
            return super.preHandle(request, response, handler);
        }
    }

    /**
     * 验证同一个url数据是否相同提交  ,相同返回true，不相同返回false,
     * 具体规则交由子类实现
     * @param request
     * @return
     */
    public abstract Integer repeatDataValidator(HttpServletRequest request, HttpServletResponse response, SameUrlData annotation);
}
