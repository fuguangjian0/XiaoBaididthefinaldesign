package com.fgj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fgj.entity.User;
import com.fgj.entity.dto.UserDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 付广建
 * @since 2023-09-02
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    void register(UserDTO userDTO);

    Page<User> findPage(Page<User> objectPage, String username, String email, String address);

}
