package org.jfinger.cloud.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.system.feign.JlbSmsApi;
import org.jfinger.cloud.system.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 就来帮短信发送实现
 * @Author finger
 * @Date 2021/3/11 0011
 * @Version 1.0
 */
@Slf4j
@Configuration
public class JlbSmsServiceImpl implements ISmsService {

    @Autowired
    private JlbSmsApi jlbSmsApi;

    @Value("${spring.sms.accessKeyId}")
    private String key;

    @Value("${spring.sms.accessKeySecret}")
    private String secret;

    @Override
    public Result<String> sendSms(String phone, String content) {
        if (StringUtils.isEmpty(key))
            Result.fail("SiKey未配置");
        if (StringUtils.isEmpty(phone))
            Result.fail("手机号不能为空");
        if (StringUtils.isEmpty(content))
            Result.fail("内容不能为空");
        Map<String, Object> formParams = new HashMap<>();
        formParams.put("sikey", key);
        formParams.put("phones", phone);
        formParams.put("content", content);
        formParams.put("typeid", 1);
        JSONObject json = jlbSmsApi.sendSms(formParams);
        log.debug("短信接口返回：" + json.toJSONString());
        if (json != null) {
            JSONObject smsResult = json.getJSONObject("data");
            if (smsResult != null && smsResult.getBoolean("result")) {
                return Result.success();
            }
        }
        return Result.fail("短信发送失败！");
    }
}
