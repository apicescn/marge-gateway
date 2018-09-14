package com.szss.marge.common.test;

import java.lang.reflect.Field;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @author XXX
 * @date 2018/3/14
 */
@Slf4j
public class MultiValueMapTools {

    /**
     * 把对象映射成MultiValueMap对象,如果属性为null则不会映射，主要用在查询中
     * 
     * @author XXX
     * @param obj 源对象
     * @return MultiValueMap对象
     */
    public static final MultiValueMap object2MultiValueMapWithoutNullProperty(Object obj) {
        MultiValueMap map = new LinkedMultiValueMap();
        try {
            Class type = obj.getClass();
            Field[] fields = type.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {

                String property = fields[i].getName();
                fields[i].setAccessible(true);
                Object filedValue = fields[i].get(obj);
                if (filedValue != null) {
                    map.add(property, filedValue.toString());
                }
            }
        } catch (Exception e) {
            log.error("object2MultiValueMapWithoutNullProperty(obj={}) 对象转换异常 Exception. ErrorMsg:{}", obj,
                e.getMessage(), e);
        }
        return map;
    }

    /**
     * 把对象映射成MultiValueMap对象
     *
     * @param obj 源对象
     * @return MultiValueMap对象
     */
    public static final MultiValueMap object2MultiValueMap(Object obj) {
        MultiValueMap map = new LinkedMultiValueMap();
        try {
            Class type = obj.getClass();
            Field[] fields = type.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String property = fields[i].getName();
                fields[i].setAccessible(true);
                Object filedValue = fields[i].get(obj);
                String value = null;
                if (filedValue != null) {
                    value = filedValue.toString();
                }
                map.add(property, value);
            }
            return map;
        } catch (Exception e) {
            log.error("object2MultiValueMap(obj={}) 对象转换异常 Exception. ErrorMsg:{}", obj, e.getMessage(), e);
        }
        return map;
    }
}
