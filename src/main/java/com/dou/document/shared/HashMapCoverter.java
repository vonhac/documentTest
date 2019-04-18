package com.dou.document.shared;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class HashMapCoverter {

    public static final Map<String, Object> convert(Class clazz, Object instance) {
        Map<String, Object> resultMap = new HashMap<>();

        if (clazz == null || instance == null) {
            return null;
        }

        Field[] allFields = clazz.getDeclaredFields();
        for (Field field : allFields) {
            try {
                String keyName = field.getName();
                field.setAccessible(true);
                Object val = field.get(instance);
                if (val != null) {
                    resultMap.put(keyName, val);
                }
            } catch (Exception e) {
                continue;
            }
        }

        return resultMap;
    }

}
