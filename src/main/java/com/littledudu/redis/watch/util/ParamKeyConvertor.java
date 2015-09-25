package com.littledudu.redis.watch.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2015-9-25
 * @author hujinjun
 */
public class ParamKeyConvertor {
    public static <T> T setupByParamKey(Map<String, String> map, Class<T> clazz) {
        try {
            T value = clazz.newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ParamKey.class)) {
                    ParamKey paramKey = field.getAnnotation(ParamKey.class);
                    String keyName = paramKey.value();
                    String str = map.get(keyName);
                    if(str == null) {
                    	continue;
                    }
                    
                    String fieldName = field.getName();
                    String setter = "set" + fieldName.toUpperCase().substring(0, 1) + fieldName.substring(1, fieldName.length());
                    Class<?> fieldType = field.getType();
                    Method m = clazz.getMethod(setter, fieldType);
                    
                    Object someObject = null;
                    if(fieldType.equals(Integer.class) || fieldType.equals(int.class))
                    	someObject = Integer.valueOf(str);
                    else if(fieldType.equals(Long.class) || fieldType.equals(long.class))
                    	someObject = Long.valueOf(str);
                    else if(fieldType.equals(Double.class) || fieldType.equals(double.class))
                    	someObject = Double.valueOf(str);
                    else if(fieldType.equals(Float.class) || fieldType.equals(float.class))
                    	someObject = Float.valueOf(str);
                    else if(fieldType.equals(String.class)) {
                    	someObject = str;
                    }
                    
                    if(m != null) {
                        m.invoke(value, someObject);
                    }
                }
            }

            return value;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Deprecated
    public static Map<String, Double> getParamMap(Object value) {
        Class<?> clazz = value.getClass();
        Map<String, Double> map = new HashMap<String, Double>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ParamKey.class)) {
                ParamKey paramKey = field.getAnnotation(ParamKey.class);
                String keyName = paramKey.value();
                String fieldName = field.getName();
                String getter = "get" + fieldName.toUpperCase().substring(0, 1) + fieldName.substring(1, fieldName.length());
                
                try {
                    Method m = clazz.getMethod(getter);
                    Object obj = null;
                    if(m != null) {
                        obj = m.invoke(value);
                    } else {
                        obj = field.get(value);
                    }
                    if(obj instanceof Long) {
                        double d = ((Long) obj).doubleValue();
                        map.put(keyName, d);
                    } else if(obj instanceof Integer) {
                        double d = ((Integer) obj).doubleValue();
                        map.put(keyName, d);
                    } else if(obj instanceof Float) {
                        double d = ((Float) obj).doubleValue();
                        map.put(keyName, d);
                    } else if(obj instanceof Double) {
                        map.put(keyName, (Double) obj);
                    }
                } catch (Exception e) {
                }
            }
        }
        return map;
    }
}
