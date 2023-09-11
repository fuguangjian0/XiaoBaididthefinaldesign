package com.fgj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgj.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 付广建
 * @since 2023-09-02
 */
public interface UserMapper extends BaseMapper<User> {


    Page<User> findPage(Page<User> page, @Param("username") String username,@Param("email") String email,@Param("address") String address);


}
