package org.jfinger.cloud.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jfinger.cloud.annotation.Dict;
import org.jfinger.cloud.api.SysCommonApi;
import org.jfinger.cloud.constant.CommonConstant;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.utils.reflect.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 字典aop类
 * @Author: dangzhenghui
 * @Date: 2019-3-17 21:50
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class DictAspect {

    @Autowired
    private SysCommonApi sysCommonApi;

    // 定义切点Pointcut
    @Pointcut("execution(public * org.jfinger.cloud.*.*Controller.*(..))")
    public void transPointCut() {
    }

    @Around("transPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        transDictText(result);
        long end = System.currentTimeMillis();
        log.debug("解析注入JSON数据  耗时" + (end - start) + "ms");
        return result;
    }

    /**
     * 翻译字典值为对应的字典文本
     *
     * @param result
     */
    private void transDictText(Object result) {
        if (!(result instanceof Result))
            return;
        Object data = ((Result) result).getResult();
        if (data instanceof IPage) {
            //分页时的处理
            List<JSONObject> items = new ArrayList<>();
            for (Object record : ((IPage) data).getRecords()) {
                items.add(transEntity(record));
            }
            ((IPage) data).setRecords(items);
        } else {
            //非分页实体时处理
            ((Result) result).setResult(transEntity(data));
        }
    }

    /**
     * 翻译实体内的字典数据
     *
     * @param object
     * @return
     */
    private JSONObject transEntity(Object object) {
        JSONObject item = (JSONObject) JSON.toJSON(object);
        for (Field field : ClassUtils.getAllFields(object)) {
            if (field.getAnnotation(Dict.class) != null) {
                String code = field.getAnnotation(Dict.class).dicCode();
                String text = field.getAnnotation(Dict.class).dicText();
                String table = field.getAnnotation(Dict.class).dictTable();
                String key = String.valueOf(item.get(field.getName()));
                //翻译字典值对应的txt
                String textValue = translateDictValue(code, text, table, key);
                log.debug(" __翻译字典字段__ " + field.getName() + CommonConstant.DICT_TEXT_SUFFIX + "： " + textValue);
                item.put(field.getName() + CommonConstant.DICT_TEXT_SUFFIX, textValue);
            }
        }
        return item;
    }

    /**
     * 翻译字典文本
     *
     * @param code
     * @param text
     * @param table
     * @param key
     * @return
     */
    private String translateDictValue(String code, String text, String table, String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        StringBuffer textValue = new StringBuffer();
        String[] keys = key.split(",");
        for (String k : keys) {
            String tmpValue = null;
            log.debug(" 字典 key : " + k);
            if (k.trim().length() == 0) {
                continue; //跳过循环
            }
            if (!StringUtils.isEmpty(table)) {
                log.debug("--DictAspect------dicTable=" + table + " ,dicText= " + text + " ,dicCode=" + code);
                tmpValue = sysCommonApi.queryTableDictTextByKey(table, text, code, k.trim());
            } else {
                tmpValue = sysCommonApi.queryDictTextByKey(code, k.trim());
            }
            if (tmpValue != null) {
                if (!"".equals(textValue.toString())) {
                    textValue.append(",");
                }
                textValue.append(tmpValue);
            }
        }
        return textValue.toString();
    }
}
