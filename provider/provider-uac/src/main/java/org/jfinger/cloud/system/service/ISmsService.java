package org.jfinger.cloud.system.service;

import org.jfinger.cloud.entity.Result;

/**
 * @Description 短信服务
 * @Author finger
 * @Date 2021/3/11 0011
 * @Version 1.0
 */
public interface ISmsService {

    String buildCodeSms(Object template,String captcha);

    Result<String> sendSms(String phone, String content);
}
