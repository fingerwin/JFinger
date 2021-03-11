package org.jfinger.cloud.api.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jfinger.cloud.api.SysDepartApi;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.data.SysDepart;

import java.util.List;

@Slf4j
public class SysDepartApiFallback implements SysDepartApi {

    @Setter
    private Throwable cause;

    @Override
    public Result<SysDepart> getById(String id) {
        log.info("--获取部门信息异常--id:" + id, cause);
        return null;
    }

    @Override
    public Result<List<String>> getSubDepIdsByDepId(String id) {
        log.info("--获取子级部门信息异常--id:" + id, cause);
        return null;
    }
}
