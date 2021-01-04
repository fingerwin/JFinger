package org.jfinger.cloud.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description 系统日志
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
@Data
public class SysLog {
    private long id;

    private int logType;

    private String logContent;

    private int operateType;

    private String userId;

    private String userName;

    private String ip;

    private String method;

    private String requestUrl;

    private String requestParam;

    private String requestType;

    private long costTime;

    private String createBy;

    private Date createTime;

    private Date modifyTime;

    private String updateBy;
}
