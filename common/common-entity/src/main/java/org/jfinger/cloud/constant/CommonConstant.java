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
    public static final Integer STATUS_DISABLED = 0;

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

    /**
     * 字典翻译文本后缀
     */
    public static final String DICT_TEXT_SUFFIX = "_dictText";

    /**
     * 用户信息
     */
    public static final String SYS_USER_INFO = "SYS_USER_INFO";

    /**
     * 登录用户Token令牌缓存KEY前缀
     */
    public static final String PREFIX_USER_TOKEN = "prefix_user_token_";

    /**
     * 登录用户Shiro权限缓存KEY前缀
     */
    public static String PREFIX_USER_SHIRO_CACHE = "shiro:cache:org.jfinger.cloud.shiro.authc.ShiroRealm.authorizationCache:";

}
