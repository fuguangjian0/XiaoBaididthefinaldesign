package com.fgj.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgj.common.Constants;
import com.fgj.entity.Menu;
import com.fgj.entity.User;
import com.fgj.entity.dto.UserDTO;
import com.fgj.exception.ServiceException;
import com.fgj.mapper.RoleMapper;
import com.fgj.mapper.RoleMenuMapper;
import com.fgj.mapper.UserMapper;
import com.fgj.service.IMenuService;
import com.fgj.service.IUserService;
import com.fgj.util.SetTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 付广建
 * @since 2023-09-02
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;
    
    @Resource
    private IMenuService menuService;

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDTO login(UserDTO userDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername()).eq(User::getPassword, userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查到信息
            log.info(" 服务调用到 登录 查询 数据库 是否存在此 用户信息："+one);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            log.info(" 服务调用到 登录 设置token 到session中 :  {}", userDTO);
            // 设置token
            userDTO.setToken(SetTokenUtil.genToken(String.valueOf(one.getId()), one.getPassword()));
            // 查到用户对应的角色名称
            String roleFlag = one.getRole(); // ROLE_ADMIN
            // 根据角色名称查到角色ID
            Integer roleId = roleMapper.getRoleIdByRoleName(roleFlag);
            // 根据角色ID查到对应的菜单ID集合
            int[] menuIds = roleMenuMapper.getIdsById(roleId);
            // 把数组类型menuIds转成List集合
            List<Integer> menuIdList = Arrays.stream(menuIds).boxed().collect(Collectors.toList());
            // 查询菜单ID的所有菜单项
            List<Menu> menus = menuService.findMenus("");
            // new一个空的最后筛选完成之后的list
            List<Menu> roleMenus = new ArrayList<>();
            // 筛选当前用户角色的菜单
            for (Menu menu : menus) {
                if (menuIdList.contains(menu.getId()))
                    roleMenus.add(menu);
                List<Menu> children = menu.getChildren();
                // removeIf 移除不存在的子节点
                boolean isSuccess = children.removeIf(child -> !menuIdList.contains(child.getId()));
                // 如果查不到,弹幕说这样搞
//                if (children.size() != 0 && isSuccess)
//                    roleMenus.add(menu);
            }
            userDTO.setMenus(roleMenus);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public void register(UserDTO userDTO) {
        // 获得 username password 往数据库里插入
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        // 如果数据库有重复的用户名, 则报错 => '用户名重复'
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            log.info(String.valueOf(e));
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        if (one != null) {
            // 已经存在, 是否需要做缓存
            throw new ServiceException(Constants.CODE_400, "用户名已存在");
        }
        // 否则, 成功
        else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            try {
                save(user);
            } catch (Exception e) {
                log.info(String.valueOf(e));
                throw new ServiceException(Constants.CODE_500, "系统错误");
            }
        }
    }

    @Override
    public Page<User> findPage(Page<User> page, String username, String email, String address) {
        return userMapper.findPage(page, username, email, address);

    }

}

