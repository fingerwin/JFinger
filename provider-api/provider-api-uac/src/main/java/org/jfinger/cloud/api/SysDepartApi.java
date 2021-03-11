package org.jfinger.cloud.api;

import io.swagger.annotations.ApiOperation;
import org.jeecg.modules.api.factory.SysDepartRemoteApiFallbackFactory;
import org.jfinger.cloud.constant.ServiceConstant;
import org.jfinger.cloud.entity.Result;
import org.jfinger.cloud.entity.bo.SysDepart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Description: 系统部门对外api
 * @author: Mikazuki
 * @date: 2020年8月21日16:17:48
 */
@Component
@FeignClient(contextId = "SysDepartRemoteApi", value = ServiceConstant.PROVIDER_UAC, fallbackFactory = SysDepartRemoteApiFallbackFactory.class)
public interface SysDepartRemoteApi {

    @GetMapping("/sys/sysDepart/{id}")
    Result<SysDepart> getById(@PathVariable("id") String id);

    @GetMapping("/sys/sysDepart/sub/{id}")
    @ApiOperation(value = "查询指定Id的部门信息", notes = "查询指定Id的部门信息")
    Result<List<String>> getSubDepIdsByDepId(@PathVariable String id);
}
