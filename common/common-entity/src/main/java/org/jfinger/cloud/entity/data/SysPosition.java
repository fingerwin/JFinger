package org.jfinger.cloud.entity.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Data
@TableName("sys_position")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_position对象", description = "职务表")
public class SysPosition implements Serializable {

    private static final long serialVersionUID = 2738515452628557293L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 职务编码
     */
    @ApiModelProperty(value = "职务编码")
    private String code;

    /**
     * 职务名称
     */
    @ApiModelProperty(value = "职务名称")
    private String name;

    /**
     * 职级
     */
    @ApiModelProperty(value = "职级")
    @Dict(dicCode = "position_rank")
    private String postRank;

    /**
     * 公司id
     */
    @ApiModelProperty(value = "部门id")
    private Integer departId;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

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
