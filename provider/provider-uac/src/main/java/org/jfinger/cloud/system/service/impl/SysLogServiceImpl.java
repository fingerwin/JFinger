package org.jfinger.cloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jfinger.cloud.data.SysLog;
import org.jfinger.cloud.entity.uac.LoginUser;
import org.jfinger.cloud.enumerate.LogType;
import org.jfinger.cloud.enumerate.OperateType;
import org.jfinger.cloud.system.mapper.SysLogMapper;
import org.jfinger.cloud.system.service.ISysLogService;
import org.jfinger.cloud.utils.common.SpringContextUtils;
import org.jfinger.cloud.utils.network.IpUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 系统日志服务
 * @Author finger
 * @Date 2021/3/5 0005
 * @Version 1.0
 */
@Service
@Slf4j
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public void addLog(SysLog sysLog) {
        try {
            sysLogMapper.saveLog(sysLog);
        } catch (Exception e) {
            log.warn(" LogContent length : " + sysLog.getLogContent().length());
            log.warn(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, LogType logType, OperateType operateType, LoginUser user) {
        SysLog sysLog = new SysLog();
        //注解上的描述,操作日志内容
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operateType);
        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(IpUtils.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        //获取登录用户信息
        if (user == null) {
            try {
                user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        if (user != null) {
            sysLog.setUserId(user.getUserName());
            sysLog.setUserName(user.getRealName());
        }
        sysLog.setCreateTime(new Date());
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            sysLogMapper.saveLog(sysLog);
        } catch (Exception e) {
            log.warn(" LogContent length : " + sysLog.getLogContent().length());
            log.warn(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, LogType logType, OperateType operateType) {
        addLog(logContent, logType, operateType, null);
    }

    @Override
    public void removeAll() {

    }

    @Override
    public Long countTotalVisit() {
        return null;
    }

    @Override
    public Long countTotalVisit(Date dayStart, Date dayEnd) {
        return null;
    }

    @Override
    public List<Map<String, Object>> countVisitAndIp(Date dayStart, Date dayEnd) {
        return null;
    }
}
