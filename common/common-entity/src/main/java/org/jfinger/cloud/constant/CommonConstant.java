package org.jfinger.cloud.constant;

/**
 * @Description 通用常量
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
public class CommonConstant {

    /**
     * ACCESS_TOKEN
     */
    public final static String X_ACCESS_TOKEN = "X-Access-Token";

    /**
     * 正常状态
     */
    public static final Integer STATUS_NORMAL = 1;

    /**
     * 禁用状态
     */
    public static final Integer STATUS_DISABLE = 0;

    /**
     * 删除状态
     */
    public static final Integer STATUS_DELETE = -1;

    /**
     * Token缓存时间：3600秒即一小时
     */
    public static final Integer TOKEN_EXPIRE_TIME = 3600;

    /**
     * 自定义错误码起始值
     */
    public static final Integer RSP_BUSINESS_BEGIN = 1000;

}
