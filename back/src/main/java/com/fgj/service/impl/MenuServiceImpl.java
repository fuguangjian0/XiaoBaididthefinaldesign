package com.fgj.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fgj.entity.Menu;
import com.fgj.mapper.MenuMapper;
import com.fgj.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 付广建
 * @since 2023-09-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name))
            queryWrapper.like("name", name);
        // 查询所有菜单
        List<Menu> list = this.list(queryWrapper);
        // 筛选一级菜单 pid = null
        List<Menu> parentNode = list.stream().filter(m -> m.getPid() == null).collect(Collectors.toList());
        // 找出一级菜单的子菜单
        for (Menu menu : parentNode) {
            List<Menu> collect = list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList());
            menu.setChildren(collect);
        }
        return parentNode;
    }


}
