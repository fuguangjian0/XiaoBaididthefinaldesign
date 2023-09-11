package com.fgj.mapper;

import com.fgj.entity.FrontImg;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FrontImgMapper {

    @Select("SELECT * FROM front_img")
    List<FrontImg> getImgList();

}
