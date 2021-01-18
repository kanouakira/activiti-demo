package com.cqkj.activiti6demo.utils;

import java.lang.reflect.Field;

/**
 * 通用的工具类
 */
public class CommonUtil {
    /**
     * 父类强转子类，通过反射循环赋值
     * @param parent
     * @param subClass
     * @return
     */
    public static Object parent2sub(Object parent, Object subClass){
        // 获取所有属性
        Field[] parents = parent.getClass().getDeclaredFields();
        // 获取父类所有属性
        Field[] children = subClass.getClass().getSuperclass().getDeclaredFields();
        try {
            for (final Field field : parents) {
                field.setAccessible(true);
                String name = field.getName();
                Object o = field.get(parent);
                for (Field child : children) {
                    child.setAccessible(true);
                    if (child.getName().equals(name)){
                        child.set(subClass, o);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return subClass;
    }
}
