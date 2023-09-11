package com.fgj.service;

import com.fgj.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 神里绫华
 * @since 2023-09-10
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> findCommentDetail(String articleId);

}
