package com.fgj.service.impl;

import com.fgj.entity.Comment;
import com.fgj.mapper.CommentMapper;
import com.fgj.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 神里绫华
 * @since 2023-09-10
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findCommentDetail(String articleId) {
        return commentMapper.findCommentDetail(articleId);
    }
}
