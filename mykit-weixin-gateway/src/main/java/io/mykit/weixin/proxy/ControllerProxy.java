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

import com.alibaba.fastjson.JSONObject;
import io.mykit.wechat.utils.common.StringUtils;
import io.mykit.weixin.constants.code.MobileHttpCode;
import io.mykit.weixin.utils.resp.helper.ResponseHelper;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;

/**
 * @author binghe
 * @version 1.0.0
 * @description 控制器代理
 */
public class ControllerProxy implements MethodInterceptor {
    private final Logger logger = LoggerFactory.getLogger(ControllerProxy.class);
    private BaseController controllerObj;
    private static final String PARAMETER = "parameter";
    private static final String ENCODING_START_LARGE = "%7B";
    private static final String ENCODING_END_LARGE = "%7D";
    private static final String ENCODING_START_SMALL = "%7b";
    private static final String ENCODING_END_SMALL = "%7d";
    private static final String CHARACTER_END = "&";

    private ControllerProxy(BaseController controllerObj) {
        super();
        this.controllerObj = controllerObj;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(BaseController controllerObj){
        ControllerProxy obj = new ControllerProxy(controllerObj);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(controllerObj.getClass());
        enhancer.setCallback(obj);
        return (T)enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        //去除访问限制
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
//        String methodName = method.getName();
        //判断调用的方法是否是RequestMapping方法，不是则不执行附加操作
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if(requestMapping == null){
            return method.invoke(controllerObj,args);
        }
        //---------------------------执行到这里说明是调用的RequestMapping方法--------------------------------
        //获取参数方法名称
        String[] parameterNames = getMethodParamNames(controllerObj.getClass(),method);//getParamName(controllerObj.getClass(), methodName,method.getParameterTypes());
        if(parameterNames == null){
            logger.error("获取到的参数方法名称数组为空");
            return method.invoke(controllerObj, args);
        }
        //获取parameter参数位置
        Integer pIndex = null;
        for(int i = 0; i < parameterNames.length; i++){
            String parameterName = parameterNames[i];
            if(PARAMETER.equals(parameterName) && !(args[i] instanceof String)){
                pIndex = i;
                break;
            }
        }
        //如果存在parameter参数，则试图去找相应的请求参数，并将其转换为对象传给方法
        if(pIndex != null){
            String pStr = controllerObj.getRequest().getParameter(PARAMETER);
            if (StringUtils.isEmpty(pStr)) {
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, controllerObj.getResponse());
                return null;
            }
            logger.debug("解码前的参数：{}", pStr);
            //处理是否编码的问题
            try{
                pStr = this.getDecodeString(pStr);
            }catch (UnsupportedEncodingException e){
                logger.error("解码参数异常：{}", e);
            }
           logger.debug("解码后的参数：{}", pStr);
            //进行数据转码
            JSONObject jsonObject = JSONObject.parseObject(pStr);
            if (jsonObject == null) {
                ResponseHelper.responseMessage(null, false, true, MobileHttpCode.HTTP_PARAMETER_INVALID, controllerObj.getResponse());
                return null;
            }
            //解码操作
            Object params = jsonObject.toJavaObject(args[pIndex].getClass());
            args[pIndex] = params;
        }else{
            logger.error("获取到的pIndex的值为: {}", pIndex);
        }
        //调用入口方法
        return method.invoke(controllerObj, args);
    }

    private String getDecodeString(String pStr) throws UnsupportedEncodingException {
        //判断是否编码字符串，已经编码，此时需要解码
        if((pStr.contains(ENCODING_START_LARGE) && pStr.contains(ENCODING_END_LARGE)) || (pStr.contains(ENCODING_START_SMALL) && pStr.contains(ENCODING_END_SMALL))){
            pStr = URLDecoder.decode(pStr, "UTF-8");
        }
        if(pStr.endsWith(CHARACTER_END)){
            pStr = pStr.substring(0, pStr.lastIndexOf(CHARACTER_END));
        }
        return pStr;
    }

    /**
     *
     * <p>
     * 获取方法的参数名
     * </p>
     *
     * @param m
     * @return
     */
    public static String[] getMethodParamNames(Class<?> clazz, final Method m) {
        final String[] paramNames = new String[m.getParameterTypes().length];
        ClassReader cr = null;
        try {
            String className = clazz.getName();
            int lastDotIndex = className.lastIndexOf(".");
            String classFileName = className.substring(lastDotIndex + 1) + ".class";
            InputStream is = clazz.getResourceAsStream(classFileName);
            cr = new ClassReader(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cr.accept(new ClassVisitor(Opcodes.ASM4) {
            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(desc);
                // 方法名相同并且参数个数相同
                if (!name.equals(m.getName()) || !sameType(args, m.getParameterTypes())) {
                    return super.visitMethod(access, name, desc, signature,  exceptions);
                }
                MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM4, v) {
                    @Override
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(m.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < paramNames.length) {
                            paramNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }

                };
            }
        }, 0);
        return paramNames;
    }

    /**
     *
     * <p>
     * 比较参数类型是否一致
     * </p>
     *
     * @param types
     *            asm的类型({@link Type})
     * @param clazzes
     *            java 类型({@link Class})
     * @return
     */
    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
        // 个数不同
        if (types.length != clazzes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(clazzes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }
}
