package com.fgj.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgj.common.Result;
import com.fgj.entity.Comment;
import com.fgj.service.ICommentService;
import com.fgj.util.SetTokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 神里绫华
 * @since 2023-09-10
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Comment comment) {
        if (comment.getId() == null) { // 新增评论
            comment.setUserId(SetTokenUtil.getCurrentUser().getId());
            comment.setTime(DateUtil.now());

            if (comment.getPid() != null) {
                Integer pid = comment.getPid();
                Comment pComment = commentService.getById(pid);
                if (pComment.getOriginId() != null) {  // 如果当前回复的父级有祖宗, 那么设置相同的祖宗
                    comment.setOriginId(pComment.getOriginId());
                } else {  // 否则设置父级为当前回复的祖宗
                    comment.setOriginId(comment.getPid());
                }
            }
        }
        commentService.saveOrUpdate(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        commentService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        commentService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(commentService.list());
    }

    // 通过文章id查到所有评论
    @GetMapping("/tree/{articleId}")
    public Result findTree(@PathVariable String articleId) {
        // 查询所有评论,包括回复的评论
        List<Comment> articleComments = commentService.findCommentDetail(articleId);
        // 给定的文章评论列表中过滤出`没有原始评论的评论`，并将其收集到新的列表中: 不是回复评论， 第一次的评论
        List<Comment> originList = articleComments.stream().filter(item -> item.getOriginId() == null).collect(Collectors.toList());

        // 设置评论数据的子节点,也就是回复的内容
        for (Comment origin : originList) {
            List<Comment> comments = articleComments.stream().filter(item -> origin.getId().equals(item.getOriginId())).collect(Collectors.toList());
            comments.forEach(comment -> {
                articleComments.stream().filter(c1 ->  c1.getId().equals(comment.getPid())).findFirst().ifPresent(v -> {  // 找到当前评论的父级
                    comment.setPUserId(String.valueOf(v.getUserId()));
                    comment.setPNickname(v.getNickname());
                });
            });
            origin.setChildren(comments);
        }
        return Result.success(originList);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(commentService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(commentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}










