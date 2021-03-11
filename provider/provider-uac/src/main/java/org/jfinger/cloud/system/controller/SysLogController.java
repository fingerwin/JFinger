package org.jfinger.cloud.system.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.data.SysLog;
import org.jfinger.cloud.entity.data.SysRole;
import org.jfinger.cloud.system.service.ISysLogService;
import org.jfinger.cloud.utils.common.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @Author finger
 * @since 2021-3-5
 */
@RestController
@RequestMapping("/sys/log")
@Slf4j
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    /**
     * 查询日志记录
     *
     * @param logType
     * @param createTime_begin
     * @param createTime_end
     * @param column
     * @param order
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysLog>> queryLogs(@RequestParam(name = "logType", defaultValue = "1") Integer logType,
                                           @RequestParam(name = "createTime_begin") String createTime_begin,
                                           @RequestParam(name = "createTime_end") String createTime_end,
                                           @RequestParam(name = "column") String column,
                                           @RequestParam(name = "order") String order,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(name = "keyWord", defaultValue = "") String keyWord) {
        Result<IPage<SysLog>> result = Result.success();
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("log_type", logType);
        if (!StringUtils.isEmpty(createTime_begin)) {
            queryWrapper.ge("create_time", createTime_begin);
            queryWrapper.le("create_time", createTime_end);
        }
        if (!StringUtils.isEmpty(column)) {
            if (order.toUpperCase().indexOf("ASC") >= 0) {
                queryWrapper.orderByAsc(FormatUtils.camelToUnderline(column));
            } else {
                queryWrapper.orderByDesc(FormatUtils.camelToUnderline(column));
            }
        }
        Page<SysLog> page = new Page<SysLog>(pageNo, pageSize);
        //日志关键词
        if (!StringUtils.isEmpty(keyWord)) {
            queryWrapper.like("log_content", keyWord);
        }
        //创建时间/创建人的赋值
        IPage<SysLog> pageList = sysLogService.page(page, queryWrapper);
        result.setResult(pageList);
        return result;
    }

    /**
     * 删除单个日志记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<SysLog> delete(@RequestParam(name = "id", required = true) String id) {
        SysLog sysLog = sysLogService.getById(id);
        if (sysLog == null) {
            return Result.fail("未找到对应实体");
        } else {
            if (sysLogService.removeById(id))
                return Result.success("删除成功!", sysLog);
            else
                return Result.fail("删除失败");
        }
    }

    /**
     * 批量，全部清空日志记录
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<SysLog> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysRole> result = new Result<SysRole>();
        if (ids == null || "".equals(ids.trim())) {
            return Result.fail("参数不识别！");
        } else {
            if ("allclear".equals(ids)) {
                sysLogService.removeAll();
                return Result.success("清除成功!");
            }
            sysLogService.removeByIds(Arrays.asList(ids.split(",")));
            return Result.success("删除成功!");
        }
    }

    /**
     * 包存日志
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/save")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        Result<?> result = new Result<>();
        SysLog log = JSON.parseObject(jsonObject.toJSONString(), SysLog.class);
        sysLogService.save(log);
        return result;
    }

}
