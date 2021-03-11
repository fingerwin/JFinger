package org.jfinger.cloud.entity.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 菜单权限表
 * </p>
 *
 * @Author finger
 * @since 2021-2-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
@ApiModel(value = "sys_permission表对象", description = "权限表")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = -1136917646371399027L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "Id")
    private Integer id;

    /**
     * 父id
     */
    @ApiModelProperty(value = "父Id")
    private Integer parentId;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String name;

    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String url;

    /**
     * 组件
     */
    @ApiModelProperty(value = "组件")
    private String component;

    /**
     * 组件名字
     */
    @ApiModelProperty(value = "组件名字")
    private String componentName;

    /**
     * 一级菜单跳转地址
     */
    @ApiModelProperty(value = "一级菜单跳转地址")
    private String redirect;

    /**
     * 类型（0：一级菜单；1：子菜单 ；2：按钮权限）
     */
    @Dict(dicCode = "menu_type")
    @ApiModelProperty(value = "类型")
    private Integer menuType;

    /**
     * 菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开
     */
    @ApiModelProperty(value = "菜单权限编码")
    private String perms;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;

    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    @TableField(value = "route_menu")
    @ApiModelProperty(value = "是否路由菜单")
    private boolean route;

    /**
     * 是否叶子节点: 1:是  0:不是
     */
    @ApiModelProperty(value = "是否叶子节点")
    private boolean leaf;


    /**
     * 是否缓存页面: 0:不是  1:是（默认值1）
     */
    @ApiModelProperty(value = "是否缓存页面")
    private boolean keepAlive;

    /**
     * 是否隐藏路由: 0:不是  1:是（默认值0）
     */
    @ApiModelProperty(value = "是否隐藏路由")
    private boolean hidden;

    /**
     * 是否聚合子路由: 0:不是  1:是（默认值1）
     */
    @ApiModelProperty(value = "是否聚合子路由")
    private boolean alwaysShow;

    /**
     * 外链菜单是否外部打开: 0:不是  1:是（默认值1）
     */
    @ApiModelProperty(value = "外链菜单是否外部打开")
    private boolean target;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 状态
     */
    @Dict(dicCode = "status")
    @TableLogic(delval = "-1")
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
