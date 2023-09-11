package com.fgj.service.impl;

import com.fgj.entity.Article;
import com.fgj.mapper.ArticleMapper;
import com.fgj.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 神里绫华
 * @since 2023-09-09
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
