package com.yzw.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yzw.entity.Article;
import com.yzw.mapper.ArticleMapper;
import com.yzw.model.DTO.ArticleFooter;
import com.yzw.model.DTO.ArticleHead;
import com.yzw.model.DTO.ShowArticle;
import com.yzw.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzw.utils.BasePageResponse;
import com.yzw.utils.JsonResult;
import com.yzw.utils.SSH;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yzw
 * @since 2021-01-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${userip}")
    private String userip;
    @Value("${name}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${execCommand}")
    private String execCommand;


    @Override
    public Boolean saveArticleHead(ArticleHead articleHead) {
        ModelMapper mapper = new ModelMapper();
        Article article = mapper.map(articleHead, Article.class);
        article.setDate(LocalDateTime.now());
        article.setState(0);
        return this.save(article);

    }

    @Override
    public Boolean saveArticle(ArticleFooter articleFooter) {
        ModelMapper mapper = new ModelMapper();
        Article article = mapper.map(articleFooter, Article.class);
        article.setCreateTime(LocalDateTime.now());
        article.setState(0);
        if (this.updateById(article)) {
            stringRedisTemplate.delete(articleFooter.getId() + articleFooter.getTitle());
            return true;
        }

        return false;
    }

    @Override
    public void saveArticleRedis(ArticleFooter articleFooter) {
        stringRedisTemplate.opsForValue().set(articleFooter.getId() + articleFooter.getTitle(), articleFooter.getText());
    }

    @Override
    public BasePageResponse<Article> selectArticle(ShowArticle showArticle) {
        IPage<Article> page = new Page(showArticle.getPageNumber(), showArticle.getPageSize());
        boolean b = (StringUtils.isEmpty(showArticle.getStartTime()) && StringUtils.isEmpty(showArticle.getEndTime()));
        if (!b) {
            showArticle.setEndTime(showArticle.getEndTime().plusDays(1));
        }
        Wrapper<Article> wrapper = Wrappers.<Article>lambdaQuery().like(!StringUtils.isEmpty(showArticle.getTitle()), Article::getTitle, showArticle.getTitle())
                .like(!StringUtils.isEmpty(showArticle.getTags()), Article::getTags, showArticle.getTags())
                .like(!StringUtils.isEmpty(showArticle.getCategories()), Article::getCategories, showArticle.getCategories())
                .between(!b, Article::getDate, showArticle.getStartTime(), showArticle.getEndTime())
                .orderByDesc(Article::getDate);
        page = this.page(page, wrapper);
        page.getRecords().forEach(m -> {
            String text = stringRedisTemplate.opsForValue().get(m.getId() + m.getTitle());
            if (!StringUtils.isEmpty(text)) {
                m.setText(text);
            }
        });
        BasePageResponse<Article> pageResponse = new BasePageResponse<>();
        pageResponse.setPages(page.getPages());
        pageResponse.setCurrent(page.getCurrent());
        pageResponse.setSize(page.getSize());
        pageResponse.setTotal(page.getTotal());
        pageResponse.setRecord(page.getRecords());
        return pageResponse;
    }


    @Override
    public List<Article> selectArticle(String title) {
        Wrapper<Article> wrapper = Wrappers.<Article>lambdaQuery().eq(Article::getTitle, title);
        return this.list(wrapper);

    }

    @Override
    public Boolean updateArticleByTitle(Integer id, ArticleHead articleHead) {
        ModelMapper mapper = new ModelMapper();
        Article article = mapper.map(articleHead, Article.class);
        article.setId(id);
        article.setState(0);
        return this.updateById(article);
    }

    @Override
    public Boolean deleteArticle(Integer[] id) {
        List<Integer> list = Arrays.asList(id);
        return this.removeByIds(list);

    }

    @Override
    public Boolean synArticle(Integer id, String filePath, String suffix) {


        LambdaQueryWrapper<Article> eq = Wrappers.<Article>lambdaQuery().eq(Article::getId, id);
        List<Article> list = this.list(eq);
        Article article = null;
        if (list.size() != 0) {
            article = list.get(0);
        }
        try {
            WriteToMDFile(article, filePath, suffix);
        } catch (IOException e) {
            log.error("同步数据异常" + e);
            return false;
        }
        article.setState(1);
        return this.updateById(article);
    }

    public static void WriteToMDFile(Article article, String filePath, String suffix) throws IOException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("---" + "\r\n");
        buffer.append("title: " + article.getTitle() + "\r\n");
        buffer.append("tags:" + "\r\n");
        for (String str : article.getTags().split(",")) {
            buffer.append("  - " + str + "\r\n");
        }
        buffer.append("categories:" + "\r\n");
        for (String str : article.getCategories().split(",")) {
            buffer.append("  - " + str + "\r\n");
        }
        buffer.append("cover: " + article.getCover() + "\r\n");
        String sate =  article.getDate().toString().split(":").length<3?article.getDate().toString()+":00":article.getDate().toString();
        buffer.append("date: " + sate.replace("T", " ") + "\r\n");
        buffer.append("---" + "\r\n");
        buffer.append("\r\n");
        buffer.append(article.getText());
        File file = new File(filePath, article.getTitle() + suffix);
        file.setWritable(true, false);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        try (FileWriter fw = new FileWriter(file.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(buffer.toString());
        } catch (IOException e) {
            log.error("错误的文件位置" + e);
        }
    }


    @Override
    public Boolean deployArticle() {
        String osName = System.getProperty("os.name");
        String path = System.getProperty("user.dir");
        if (osName.toLowerCase().contains("linux")) {
            SSH executor = new SSH(userip, username, password);
            return executor.exec(execCommand);
        } else if (osName.toLowerCase().contains("windows")) {
            if (runCmd("cmd /c start " + path + "\\script\\creatFile.bat d") == 0) {
                return true;
            }
        }

        return false;
    }

    public Integer runCmd(String strcmd) {
        Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
        Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
        try {
            ps = rt.exec(strcmd);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
            ps.waitFor();  //等待子进程完成再往下执行。
        } catch (IOException | InterruptedException e1) {
            log.error("批处理命令不存在" + e1);
            return 1;
        }

        int i = ps.exitValue();  //接收执行完毕的返回值
        ps.destroy();  //销毁子进程
        return i;
    }


    @Override
    public Boolean fileToDB(String filePath) {
        List<Article> articles = dloyArticle(filePath);
        if (articles != null && articles.size() > 0) {
            return this.saveBatch(articles);
        }
        return false;
    }

    @Override
    public List<Article> categorieList() {
        List<Article> list = this.list(Wrappers.<Article>lambdaQuery().groupBy(Article::getCategories).select(Article::getCategories));
        return list;
    }

    @Override
    public List<Article> tagsList() {
        List<Article> list = this.list(Wrappers.<Article>lambdaQuery().groupBy(Article::getTags).select(Article::getTags));
        return list;
    }

    public List<Article> dloyArticle(String filePath) {
        File file = new File(filePath);
        if (!file.canExecute()) {
            log.error(filePath + " 非可执行文件");
            return null;
        }
        List<Article> readerList = new ArrayList();
        if (file.isDirectory()) {
            if (!filePath.endsWith("/") || !filePath.endsWith("\\")) {
                filePath += "/";
            }
            String[] list = file.list();
            String finalFilePath = filePath;
            Arrays.stream(list).filter(v -> v.endsWith(".md")).forEach(x -> {
                readerList.add(readerfile(finalFilePath + x));
            });
        }
        if (file.isFile()) {
            readerList.add(readerfile(filePath));
        }
        return readerList;
    }

    public Article readerfile(String filePath) {
        Article article = new Article();
        StringBuffer buffer = new StringBuffer();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)))) {
            String readLine = null;
            readLine = bufferedReader.readLine();
            while ((readLine = bufferedReader.readLine()) != null) {
                if (readLine.startsWith("title:")) {
                    article.setTitle(readLine.split(" ")[1].trim());
                }
                if (readLine.startsWith("tags:")) {
                    article.setTags(readLine = bufferedReader.readLine().replace("- ", "").trim());
                }
                if (readLine.startsWith("categories:")) {
                    article.setCategories(readLine = bufferedReader.readLine().replace("- ", "").trim());
                }
                if (readLine.startsWith("cover:")) {
                    article.setCover(readLine.split(" ")[1].trim());
                }
                if (readLine.startsWith("date:")) {
                    article.setDate(LocalDateTime.parse(readLine.trim().split(" ")[1] + " " + readLine.trim().split(" ")[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
                if (readLine.equals("---")) {
                    while ((readLine = bufferedReader.readLine()) != null) {
                        buffer.append(readLine + "\r\n");
                    }
                }

            }
            article.setText(buffer.toString());
            article.setState(0);
            article.setCreateTime(LocalDateTime.now());
        } catch (IOException e) {
            log.error(filePath + " 文件路径不存在或文件不存在");
        }
        return article;
    }
}
