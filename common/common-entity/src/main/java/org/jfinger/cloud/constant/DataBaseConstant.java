package org.jfinger.cloud.constant;
/**
 * 数据库上下文常量
 */
public interface DataBaseConstant {
	//*********数据库类型****************************************
	public static final String DB_TYPE_MYSQL = "MYSQL";
	public static final String DB_TYPE_ORACLE = "ORACLE";
	public static final String DB_TYPE_POSTGRESQL = "POSTGRESQL";
	public static final String DB_TYPE_SQLSERVER = "SQLSERVER";

	// 数据库类型，对应 database_type 字典
	public static final String DB_TYPE_MYSQL_NUM = "1";
	public static final String DB_TYPE_ORACLE_NUM = "2";
	public static final String DB_TYPE_SQLSERVER_NUM = "3";
	public static final String DB_TYPE_POSTGRESQL_NUM = "4";

	/**
	 * 数据-所属机构编码
	 */
	public static final String SYS_ORG_CODE = "sysOrgCode";
	/**
	 * 数据-所属机构编码
	 */
	public static final String SYS_ORG_CODE_TABLE = "sys_org_code";
	/**
	 * 数据-所属机构编码
	 */
	public static final String SYS_MULTI_ORG_CODE = "sysMultiOrgCode";
	/**
	 * 数据-所属机构编码
	 */
	public static final String SYS_MULTI_ORG_CODE_TABLE = "sys_multi_org_code";
	/**
	 * 数据-系统用户编码（对应登录用户账号）
	 */
	public static final String SYS_USER_CODE = "sysUserCode";
	/**
	 * 数据-系统用户编码（对应登录用户账号）
	 */
	public static final String SYS_USER_CODE_TABLE = "sys_user_code";

	/**
	 * 登录用户真实姓名
	 */
	public static final String SYS_USER_NAME = "sysUserName";
	/**
	 * 登录用户真实姓名
	 */
	public static final String SYS_USER_NAME_TABLE = "sys_user_name";
	/**
	 * 系统日期"yyyy-MM-dd"
	 */
	public static final String SYS_DATE = "sysDate";
	/**
	 * 系统日期"yyyy-MM-dd"
	 */
	public static final String SYS_DATE_TABLE = "sys_date";
	/**
	 * 系统时间"yyyy-MM-dd HH:mm"
	 */
	public static final String SYS_TIME = "sysTime";
	/**
	 * 系统时间"yyyy-MM-dd HH:mm"
	 */
	public static final String SYS_TIME_TABLE = "sys_time";

}
