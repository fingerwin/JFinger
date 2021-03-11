package org.jfinger.cloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jfinger.cloud.entity.data.SysDictItem;
import org.jfinger.cloud.system.mapper.SysDictItemMapper;
import org.jfinger.cloud.system.service.ISysDictItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    @Override
    public List<SysDictItem> selectItemsByMainId(String mainId) {
        return baseMapper.selectItemsByMainId(mainId);
    }
}
