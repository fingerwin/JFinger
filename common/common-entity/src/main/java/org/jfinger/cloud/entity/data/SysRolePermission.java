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
 * 角色权限表
 * </p>
 *
 * @Author finger
 * @since 2021-2-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_permission")
@ApiModel(value = "sys_role_permission表对象", description = "角色权限表")
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 7187428170503633624L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "Id")
    private Integer id;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色Id")
    private Integer roleId;

    /**
     * 权限id
     */
    @ApiModelProperty(value = "权限Id")
    private Integer permissionId;

    public SysRolePermission(Integer roleId, Integer permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
