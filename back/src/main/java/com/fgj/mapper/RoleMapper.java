package com.fgj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fgj.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 付广建
 * @since 2023-09-05
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where flag = #{flag}")
    Integer getRoleIdByRoleName(@Param("flag") String flag);


}
