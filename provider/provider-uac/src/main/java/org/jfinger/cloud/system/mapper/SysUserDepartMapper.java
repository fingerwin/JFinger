package org.jfinger.cloud.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.jfinger.cloud.entity.data.SysUserDepart;

import java.util.List;

@Mapper
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart>{

	List<SysUserDepart> getDepartByUserId(@Param("userId") String userId);
}
