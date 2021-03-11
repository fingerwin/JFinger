package org.jfinger.cloud.entity.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jfinger.cloud.enumerate.LogType;
import org.jfinger.cloud.enumerate.OperateType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 系统日志
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
@ApiModel(value = "sys_log表对象", description = "系统日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = -5573247075508036516L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "Id")
    private Integer id;

    /**
     * 日志类型,具体见{@linkplain org.jfinger.cloud.enumerate.LogType LogType}定义
     */
    @ApiModelProperty(value = "日志类型")
    private LogType logType;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String logContent;

    /**
     * 操作类型,具体见{@linkplain org.jfinger.cloud.enumerate.OperateType OperateType}定义
     */
    @ApiModelProperty(value = "操作类型")
    private OperateType operateType;

    /**
     * 操作人用户账户
     */
    @ApiModelProperty(value = "操作人用户账户")
    private String userId;

    /**
     * 操作人用户名称
     */
    @ApiModelProperty(value = "操作人用户名称")
    private String userName;

    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址")
    private String ip;

    /**
     * 调用函数
     */
    @ApiModelProperty(value = "调用函数")
    private String functionName;

    /**
     * 请求Url
     */
    @ApiModelProperty(value = "请求Url")
    private String requestUrl;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String requestParam;

    /**
     * 请求方法
     */
    @ApiModelProperty(value = "请求方法")
    private String requestMethod;

    /**
     * 操作时长
     */
    @ApiModelProperty(value = "操作时长")
    private Long costTime;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
