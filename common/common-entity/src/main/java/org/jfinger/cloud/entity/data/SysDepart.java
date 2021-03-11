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
import org.jfinger.cloud.entity.model.DepartIdModel;
import org.jfinger.cloud.entity.model.SysDepartTreeModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 部门表
 *
 * @Author finger
 * @Since 2021-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_depart")
@ApiModel(value = "sys_depart表对象", description = "部门表")
public class SysDepart implements Serializable {

    private static final long serialVersionUID = 8076800870033250317L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "Id")
    private Integer id;

    /**
     * 父机构ID
     */
    @ApiModelProperty(value = "父Id")
    private Integer parentId;

    /**
     * 机构/部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private String orgCode;

    /**
     * 机构类型<br>
     * 0:部门<br>
     * 1:公司
     */
    @Dict(dicCode = "depart_type")
    @ApiModelProperty(value = "机构类型")
    private Integer type;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 传真
     */
    @ApiModelProperty(value = "传真")
    private String fax;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

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

    /**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<SysDepartTreeModel> treeList) {
        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setLeaf(true);
            } else {
                setEmptyChildrenAsNull(model.getChildren());
                model.setLeaf(false);
            }
        }
    }

    /**
     * queryTreeList的子方法====3====
     * 该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<SysDepartTreeModel> treeList, List<SysDepartTreeModel> recordList, List<DepartIdModel> idList) {
        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            DepartIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                SysDepartTreeModel m = recordList.get(i1);
                if (m.getParentId() != null && m.getParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    DepartIdModel dim = new DepartIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<SysDepartTreeModel> findChildren(List<SysDepartTreeModel> recordList,
                                                         List<DepartIdModel> departIdList) {

        List<SysDepartTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartTreeModel branch = recordList.get(i);
            if (branch.getParentId() != null) {
                treeList.add(branch);
                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
                departIdList.add(departIdModel);
            }
        }
        getGrandChildren(treeList, recordList, departIdList);
        //idList = departIdList;
        return treeList;
    }

    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<SysDepartTreeModel> wrapTreeDataToTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        List<SysDepartTreeModel> tree = findChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * 获取 DepartIdModel
     *
     * @param recordList
     * @return
     */
    public static List<DepartIdModel> wrapTreeDataToDepartIdTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        findChildren(records, idList);
        return idList;
    }
}
