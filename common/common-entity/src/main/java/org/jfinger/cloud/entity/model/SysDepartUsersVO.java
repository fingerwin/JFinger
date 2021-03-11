package org.jfinger.cloud.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDepartUsersVO implements Serializable {

    private static final long serialVersionUID = -5144208076736325571L;

    /**
     * 部门id
     */
    private Integer depId;

    /**
     * 对应的用户id集合
     */
    private List<Integer> userIdList;

}
