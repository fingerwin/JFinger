package org.jfinger.cloud.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jfinger.cloud.entity.data.SysLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 系统日志
 * @Author finger
 * @Date 2021/3/4 0004
 * @Version 1.0
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    void  saveLog(@Param("sysLog")SysLog sysLog);

    /**
     * @功能：清空所有日志记录
     */
    public void removeAll();

    /**
     * 获取时间段内访问次数
     *
     * @return Long
     */
    Long countTotalVisit(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long countTotalIp(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

    /**
     *   首页：根据时间统计访问数量/ip数量
     * @param dayStart
     * @param dayEnd
     * @return
     */
    List<Map<String,Object>> countVisitAndIp(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);
}
