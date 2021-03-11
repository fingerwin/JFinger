package org.jfinger.cloud.api.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.api.SysCommonApi;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.data.SysLog;
import org.jfinger.cloud.entity.vo.LoginUser;

/**
 * @Description 系统通用功能Fallback
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
@Slf4j
public class SysCommonApiFallback implements SysCommonApi {

    @Setter
    private Throwable cause;

    @Override
    public void saveSysLog(SysLog syslog) {
        log.info("--包存日志信息异常", cause);
    }

    @Override
    public Result<LoginUser> getUserByName(String userName) {
        log.info("--获取用户信息异常--username:" + userName, cause);
        return null;
    }

    @Override
    public String queryDictTextByKey(String code, String key) {
        log.info("--查询字典信息异常, code:" + code + ", key:" + key, cause);
        return null;
    }

    @Override
    public String queryTableDictTextByKey(String table, String text, String code, String key) {
        log.info("--查询表字典信息异常, table:" + table + ", text:" + text + ", code:" + code + ", key:" + key, cause);
        return null;
    }
}
