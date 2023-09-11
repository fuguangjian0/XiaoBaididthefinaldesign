package com.fgj.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fgj.common.Result;
import com.fgj.config.AuthAccess;
import com.fgj.entity.FrontImg;
import com.fgj.mapper.FrontImgMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fu
 */
@Slf4j
@RestController
@RequestMapping("/front")
public class FrontImgController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String FILES_KEY = "FILES_FRONT_ALL";

    @Resource
    FrontImgMapper frontImgMapper;

    @AuthAccess
    @GetMapping
    public Result getImgList() {
        // 先看看缓存里有没有数据
        String file = stringRedisTemplate.opsForValue().get(FILES_KEY);
        List<FrontImg> imgs = null;
        if (StrUtil.isBlank(file)) {
            imgs = frontImgMapper.getImgList();
            // 存入缓存
            stringRedisTemplate.opsForValue().set(FILES_KEY, JSONUtil.toJsonStr(imgs));
//            stringRedisTemplate.delete(FILES_KEY); //删除方法
        } else {
            // 如果有, 从内存中拿即可, 减轻了数据库的压力
            imgs = JSONUtil.toBean(file, new TypeReference<List<FrontImg>>() {
            }, true);
        }
        return Result.success(imgs);
    }


}
