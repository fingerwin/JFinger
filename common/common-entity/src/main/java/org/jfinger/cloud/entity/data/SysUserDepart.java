package org.jfinger.cloud.entity.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_depart")
@ApiModel(value = "sys_user_depart表对象", description = "用户部门表")
public class SysUserDepart implements Serializable {

    private static final long serialVersionUID = 570276811890185403L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "Id")
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户Id")
    private Integer userId;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门Id")
    private Integer deptId;

    public SysUserDepart(Integer userId, Integer deptId) {
        this.userId = userId;
        this.deptId = deptId;
    }
}
