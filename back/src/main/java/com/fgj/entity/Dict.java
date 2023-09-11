package com.fgj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fu
 * 字典表 图标名字 => 图标标签
 */
@Data
@TableName("sys_dict")
public class Dict {
    private String name;
    private String value;
    private String type;

}
