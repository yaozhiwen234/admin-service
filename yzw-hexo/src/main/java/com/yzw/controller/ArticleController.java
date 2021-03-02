package com.yzw.controller;

import com.yzw.config.FileProperties;
import com.yzw.entity.Article;
import com.yzw.model.DTO.ArticleFooter;
import com.yzw.model.DTO.ArticleHead;
import com.yzw.model.DTO.ShowArticle;
import com.yzw.service.IArticleService;
import com.yzw.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yzw
 * @since 2021-01-14
 */
@RestController
@RequestMapping("/api/article")
@Api(tags = "hexo博客管理")
@RequiredArgsConstructor
@PropertySource("classpath:config/application.yml")//读取application.yml文件
public class ArticleController {


    private final IArticleService iArticleService;

    private final FileProperties properties;

    private final static String CTRLS = "ctrls";  //ctrl + s 的操作动作
    private final static String ADD = "add";  //增加 的操作动作


    @Value("${suffix}")
    private String suffix;

    //添加标题头
    @ApiOperation("添加标题头")
    @PostMapping("/addTitle")
    public JsonResult saveArticleHead(@Valid @RequestBody ArticleHead articleHead) {
        List<Article> list = iArticleService.selectArticle(articleHead.getTitle());
        if (list.size() != 0) {
            return JsonResult.error("标题名重复");
        }
        if (iArticleService.saveArticleHead(articleHead)) {
            return JsonResult.success("增加标题成功");
        }
        return JsonResult.error("增加标题失败");
    }

    //添加主文章
    @ApiOperation("添加文章")
    @PutMapping("/addArticle")
    public JsonResult saveArticle( ArticleFooter articleFooter) {
        if (CTRLS.equals(articleFooter.getOperation())) {
            iArticleService.saveArticleRedis(articleFooter);
            return JsonResult.success();
        }
        if (ADD.equals(articleFooter.getOperation())) {
            if (iArticleService.saveArticle(articleFooter)) {
                return JsonResult.success("增加文章成功");
            }
        }
        return JsonResult.error("增加文章失败");
    }

    //添加主文章
    @ApiOperation("展示文章")
    @GetMapping("/showArticle")
    public JsonResult selectArticle(ShowArticle showArticle) {
        return JsonResult.success(iArticleService.selectArticle(showArticle));
    }

    //防止标题重复
    @ApiOperation("按照标题查找")
    @GetMapping("/showArticleByTitle")
    public JsonResult selectArticle(@RequestParam("title") String title) {
        List<Article> list = iArticleService.selectArticle(title);
        if (list.size() != 0) {
            return JsonResult.error("标题名称不允许重复");
        }
        return JsonResult.success();
    }

    @ApiOperation("修改标题")
    @PutMapping("/updateArticleByTitle/{id}")
    public JsonResult updateArticleByTitle(@PathVariable("id") Integer id, @RequestBody ArticleHead articleHead) {
        boolean b = iArticleService.updateArticleByTitle(id, articleHead);
        if (!b) {
            return JsonResult.error("标题修改失败");
        }
        return JsonResult.success("修改成功");
    }

    @ApiOperation("删除文章")
    @DeleteMapping("/delArticle")
    public JsonResult deleteArticle(HttpServletRequest request, @RequestParam("id") Integer[] id) {
        if (iArticleService.deleteArticle(id)) {
            return JsonResult.success("删除成功");
        }

        return JsonResult.error("删除失败");

    }

    @ApiOperation("同步数据")
    @PostMapping("/synArticle")
    public JsonResult synArticle(@RequestParam("id") Integer id) {
        if (iArticleService.synArticle(id, properties.getPath().getFilePath(), suffix)) {
            System.out.println(properties.getPath().getFilePath());
            return JsonResult.success("同步成功");
        }
        return JsonResult.error("同步失败");
    }

    @ApiOperation("发布文章")
    @PostMapping("/deployArticle")
    public JsonResult deployArticle() {
        if (iArticleService.deployArticle()) {
            return JsonResult.success("部署成功");
        }
        return JsonResult.error("部署失败");
    }

    @ApiOperation("文件夹的数据同步到数据库")
    @PostMapping("/fileWriteDb")
    public JsonResult fileWriteDb(String filePath) {
        if (iArticleService.fileToDB(filePath)) {
            return JsonResult.success("文件入库成功");
        }
        return JsonResult.error("文件入库失败");
    }

}
