package org.jfinger.cloud.utils.reflect;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 类处理工具类
 * @Author finger
 * @Date 2020/12/25 0025
 * @Version 1.0
 */
public class ClassUtils {

    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param name 名称
     * @return
     */
    public static String Camel2Line(String name) {
        if (StringUtils.isEmpty(name))
            return name;
        if (name.length() < 2) {
            return name.toLowerCase();
        }
        StringBuilder sb = new StringBuilder(name);
        int temp = 0;//定位
        //从第三个字符开始 避免命名不规范
        for (int i = 2; i < name.length(); i++) {
            if (Character.isUpperCase(name.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }
}
