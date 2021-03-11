package org.jfinger.cloud.entity.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jfinger.cloud.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "sys_user表对象", description = "用户表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = -6367528563635432048L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "Id")
    private Integer id;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    private String userName;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JSONField(deserialize = true, serialize = false)
    private String password;

    /**
     * md5密码盐
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JSONField(deserialize = true, serialize = false)
    @ApiModelProperty(value = "md5密码盐")
    private String salt;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    @Dict(dicCode = "sex")
    @ApiModelProperty(value = "性别")
    private Integer sex;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件")
    private String email;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * 工号
     */
    @ApiModelProperty(value = "工号")
    private String workNo;

    /**
     * 职务，关联职务表
     */
    @ApiModelProperty(value = "职务Id")
    private Integer pos;

    /**
     * 座机号
     */
    @ApiModelProperty(value = "座机号")
    private String telephone;

    /**
     * 部门code(当前选择登录部门)
     */
    private String orgCode;

    /**
     * 状态
     */
    @Dict(dicCode = "status")
    @TableLogic(value = "1", delval = "-1")
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
