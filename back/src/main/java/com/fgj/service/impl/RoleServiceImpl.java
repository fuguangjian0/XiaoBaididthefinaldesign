package com.fgj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgj.entity.Menu;
import com.fgj.entity.Role;
import com.fgj.entity.RoleMenu;
import com.fgj.mapper.RoleMapper;
import com.fgj.mapper.RoleMenuMapper;
import com.fgj.service.IMenuService;
import com.fgj.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 付广建
 * @since 2023-09-05
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;

    // 先删后增
    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        // 先根据角色ID删除对应的所有菜单
        int i = roleMenuMapper.deleteByRoleId(roleId);
        log.info("删除的菜单数量={}", i);
        // 再根据菜单ID数组加入到对应的角色的菜单中
        ArrayList<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) {
                // 需要补上这个父级Pid
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public int[] getIdsById(String roleId) {
        Integer rId = Integer.valueOf(roleId);
        return roleMenuMapper.getIdsById(rId);
    }


}
