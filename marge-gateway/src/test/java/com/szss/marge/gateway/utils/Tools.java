package com.szss.marge.gateway.utils;

import java.lang.reflect.Method;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @author XXX
 * @date 2018/3/14
 */
@Slf4j
public class Tools {

    public static final MultiValueMap object2MultiValueMap(Object obj) {
        MultiValueMap map = new LinkedMultiValueMap();
        try {
            Class c = obj.getClass();
            Method m[] = c.getDeclaredMethods();
            for (int i = 0; i < m.length; i++) {
                if (m[i].getName().indexOf("get") == 0) {
                    String property = m[i].getName().replaceFirst("get", "");
                    property = property.substring(0, 1).toLowerCase() + property.substring(1);
                    Object v = m[i].invoke(obj, new Object[0]);
                    String value = null;
                    if (v != null) {
                        value = v.toString();
                    }
                    map.add(property, value);
                }
            }
        } catch (Exception e) {
            log.error("object2MultiValueMap(obj={}) 对象转换为Map异常 Exception. ErrorMsg:{}", obj, e.getMessage(), e);
        }
        return map;
    }
}
