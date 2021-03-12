package org.jfinger.cloud.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.system.feign.JlbSmsApi;
import org.jfinger.cloud.system.service.ISmsService;
import org.jfinger.cloud.utils.common.SpringContextUtils;
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
@AllArgsConstructor
public class JlbSmsService implements ISmsService {

    private String siKey;

    @Override
    public Result<String> sendSms(String phone, String content) {
        JlbSmsApi jlbSmsApi = SpringContextUtils.getBean(JlbSmsApi.class);
        if (StringUtils.isEmpty(siKey))
            Result.fail("SiKey未配置");
        if (StringUtils.isEmpty(phone))
            Result.fail("手机号不能为空");
        if (StringUtils.isEmpty(content))
            Result.fail("内容不能为空");
        Map<String, Object> formParams = new HashMap<>();
        formParams.put("sikey", siKey);
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

    @Override
    public String buildCodeSms(Object template,String captcha) {
        return "您的验证码是：" + captcha + "，10分钟内有效，请勿向他人泄露您的验证码。";
    }
}
