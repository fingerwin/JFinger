package org.jfinger.cloud.entity.model;

import lombok.Data;
import org.jfinger.cloud.entity.data.SysDepart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
@Data
public class SysDepartTreeModel implements Serializable {

    private static final long serialVersionUID = -6420059077489951386L;

    /**
     * 对应SysDepart中的id字段,前端数据树中的key
     */
    private Integer key;

    /**
     * 对应SysDepart中的id字段,前端数据树中的value
     */
    private String value;

    /**
     * 对应depart_name字段,前端数据树中的title
     */
    private String title;


    private boolean isLeaf;
    // 以下所有字段均与SysDepart相同

    private Integer id;

    private Integer parentId;

    private String departName;

    private String departNameEn;

    private String departNameAbbr;

    private Integer departOrder;

    private String description;

    private String orgCategory;

    private Integer orgType;

    private String orgCode;

    private String mobile;

    private String fax;

    private String address;

    private String memo;

    private Integer status;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private List<SysDepartTreeModel> children = new ArrayList<>();

    public SysDepartTreeModel() {
    }

    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     *
     * @param sysDepart
     */
    public SysDepartTreeModel(SysDepart sysDepart) {
        this.key = sysDepart.getId();
        this.value = String.valueOf(sysDepart.getId());
        this.title = sysDepart.getDepartName();
        this.id = sysDepart.getId();
        this.parentId = sysDepart.getParentId();
        this.departName = sysDepart.getDepartName();
        this.departOrder = sysDepart.getSort();
        this.description = sysDepart.getDescription();
        this.orgType = sysDepart.getType();
        this.orgCode = sysDepart.getOrgCode();
        this.mobile = sysDepart.getMobile();
        this.fax = sysDepart.getFax();
        this.address = sysDepart.getAddress();
        this.memo = sysDepart.getRemark();
        this.status = sysDepart.getStatus();
        this.createBy = sysDepart.getCreateBy();
        this.createTime = sysDepart.getCreateTime();
        this.updateBy = sysDepart.getUpdateBy();
        this.updateTime = sysDepart.getUpdateTime();
    }

    public List<SysDepartTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<SysDepartTreeModel> children) {
        if (children == null) {
            this.isLeaf = true;
        }
        this.children = children;
    }
}
