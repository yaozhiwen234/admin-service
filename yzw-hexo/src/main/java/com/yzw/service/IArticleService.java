package com.yzw.service;

import com.yzw.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yzw.model.DTO.ArticleFooter;
import com.yzw.model.DTO.ArticleHead;
import com.yzw.model.DTO.ShowArticle;
import com.yzw.utils.BasePageResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yzw
 * @since 2021-01-14
 */
public interface IArticleService extends IService<Article> {

    Boolean saveArticleHead(ArticleHead articleHead);

    Boolean saveArticle(ArticleFooter articleFooter);

    void saveArticleRedis(ArticleFooter articleFooter);

    BasePageResponse<Article> selectArticle(ShowArticle showArticle);

    List<Article> selectArticle(String title);

    Boolean updateArticleByTitle(Integer id, ArticleHead articleHead);

    Boolean deleteArticle(Integer[] id);

    Boolean synArticle(Integer id, String filePath, String suffix);

    Boolean deployArticle();

    Boolean fileToDB(String filePath);

}
