package org.jfinger.cloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jfinger.cloud.entity.data.SysLog;
import org.jfinger.cloud.entity.vo.LoginUser;
import org.jfinger.cloud.enumerate.LogType;
import org.jfinger.cloud.enumerate.OperateType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @Author finger
 * @since 2020-12-19
 */
public interface ISysLogService extends IService<SysLog> {

    /**
     * 保存日志
     *
     * @param sysLog
     */
    void addLog(SysLog sysLog);

    /**
     * 保存日志
     *
     * @param logContent
     * @param logType
     * @param operateType
     * @param user
     */
    void addLog(String logContent, LogType logType, OperateType operateType, LoginUser user);

    /**
     * 保存日志
     *
     * @param logContent
     * @param logType
     * @param operateType
     */
    void addLog(String logContent, LogType logType, OperateType operateType);

    /**
     * 清空所有日志
     */
    public void removeAll();

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long countTotalVisit();

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long countTotalVisit(Date dayStart, Date dayEnd);

    /**
     * 首页：根据时间统计访问数量/ip数量
     *
     * @param dayStart
     * @param dayEnd
     * @return
     */
    List<Map<String, Object>> countVisitAndIp(Date dayStart, Date dayEnd);
}
