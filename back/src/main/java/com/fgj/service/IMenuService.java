package com.fgj.service;

import com.fgj.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 付广建
 * @since 2023-09-05
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);


}
