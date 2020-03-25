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
package io.mykit.weixin.proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author binghe
 * @version 1.0.0
 * @description 基础Controller
 */
public class BaseController implements FactoryBean<Object> {
    private HttpServletRequest request;
    private HttpServletResponse response;
    public BaseController controllerProxy =  null;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    @Override
    public Object getObject() throws Exception {
        if(controllerProxy == null){
            controllerProxy = ControllerProxy.getProxy(this);
        }
        return controllerProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
    public HttpServletResponse getResponse() {
        return response;
    }
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
