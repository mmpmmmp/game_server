package com.wre.game.api.handler;

import com.wre.game.api.util.BeanHelper;
import com.wre.game.api.util.JsonHelper;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component //标记为组件
public class SpringCacheKeyGenerator implements KeyGenerator {

    private final static int NO_PARAM_KEY = 0;
    private String keyPrefix = "gameapi";// key前缀，用于区分不同项目的缓存，建议每个项目单独设置


    @Override
    public Object generate(Object target, Method method, Object... params) {

        char sp = ':';
        StringBuilder strBuilder = new StringBuilder(30);
        strBuilder.append(keyPrefix);
        strBuilder.append(sp);
        // 类名
        strBuilder.append(target.getClass().getSimpleName());
        strBuilder.append(sp);
        // 方法名
        strBuilder.append(method.getName());
        strBuilder.append(sp);
        if (params.length > 0) {
            // 参数值
            for (int i = 0; i < params.length; i++) {
                Object object = params[i];
                if (i > 0) {
                    strBuilder.append("_");
                }
                if (BeanHelper.isSimpleValueType(object.getClass())) {
                    strBuilder.append(object);
                } else {
                    strBuilder.append(JsonHelper.toJson(object).hashCode());
                }
            }
        } else {
            strBuilder.append(NO_PARAM_KEY);
        }
        return strBuilder.toString();
    }


    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}