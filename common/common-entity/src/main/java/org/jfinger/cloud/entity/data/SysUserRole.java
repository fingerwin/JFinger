package org.jfinger.cloud.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @Author finger
 * @since 2021-2-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_role")
@ApiModel(value = "sys_user_role表对象", description = "用户角色表")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 20281844762365893L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "Id")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户Id")
    private Integer userId;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色Id")
    private Integer roleId;

    public SysUserRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
