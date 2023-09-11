package com.fgj.entity.dto;


import com.fgj.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author fu
 * 接收 前端登录请求的参数 ，映射为User的实体类对象。
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;

}

