package org.jfinger.cloud.system.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @Description 96871短信接口类
 * @Author finger
 * @Date 2020/7/10 0010
 * @Version 1.0
 */
@Component
@FeignClient(name = "smsc", url = "http://smsc.96871.com.cn/smsc")
public interface JlbSmsApi {

    @PostMapping(value = "/remoting/send", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    JSONObject sendSms(@RequestBody Map<String, ?> formParams);
}
