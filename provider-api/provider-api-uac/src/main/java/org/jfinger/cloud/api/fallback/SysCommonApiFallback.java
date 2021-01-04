package org.jfinger.cloud.api.fallback;

import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.api.SysCommonApi;
import org.jfinger.cloud.entity.SysLog;
import org.jfinger.cloud.fallback.ApiFallback;

/**
 * @Description 系统通用功能Fallback
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
@Slf4j
public class SysCommonApiFallback extends ApiFallback implements SysCommonApi {
    @Override
    public void saveSysLog(SysLog syslog) {
        log.info("--保存日志信息异常", cause);
    }
}
