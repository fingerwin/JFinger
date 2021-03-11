package org.jfinger.cloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jfinger.cloud.entity.data.SysDictItem;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
