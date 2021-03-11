package org.jfinger.cloud.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 在线用户信息
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

	/**
	 * 登录人id
	 */
	private Integer id;

	/**
	 * 登录人账号
	 */
	private String userName;

	/**
	 * 登录人名字
	 */
	private String realName;

	/**
	 * 登录人密码
	 */
	private String password;

	/**
	 * 身份证号
	 */
	private String idNumber;

     /**
      * 当前登录部门id
      */
    private Integer orgId;

	/**
	 * 当前登录部门code
	 */
	private String orgCode;
	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 生日
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	/**
	 * 性别（1：男 2：女）
	 */
	private Integer sex;

	/**
	 * 电子邮件
	 */
	private String email;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 职务，关联职务表
	 */
	private String pos;

	/**
	 * 工号
	 */
	private String workNo;

	/**
	 * 座机号
	 */
	private String telephone;

	/**
	 * 归属地
	 */
	private String countryCode;

	/**
	 * 状态(-1：删除 0：禁用  1：正常）
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
}
