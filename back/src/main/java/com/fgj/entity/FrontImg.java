package com.fgj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("front_img")
public class FrontImg {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String url;

}
