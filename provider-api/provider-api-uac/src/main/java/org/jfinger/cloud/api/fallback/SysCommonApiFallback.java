package org.jfinger.cloud.fallback;

import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.api.SysCommonApi;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.SysLog;
import org.jfinger.cloud.entity.uac.LoginUser;

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

    @Override
    public Result<LoginUser> getUserByName(String userName) {
        return null;
    }

    @Override
    public String queryDictTextByKey(String code, String key) {
        return null;
    }

    @Override
    public String queryTableDictTextByKey(String table, String text, String code, String key) {
        return null;
    }
}
