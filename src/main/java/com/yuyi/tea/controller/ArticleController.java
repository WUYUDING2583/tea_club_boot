package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Article;
import com.yuyi.tea.bean.Tag;
import com.yuyi.tea.common.TimeRange;
import com.yuyi.tea.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章标签列表
     * @return
     */
    @GetMapping("/tags")
    public List<Tag> getTags(){
        List<Tag> tags = articleService.getTags();
        return tags;
    }

    /**
     * 新增文章标签
     * @param tag
     * @return
     */
    @PostMapping("/admin/tag")
    @Transactional(rollbackFor = Exception.class)
    public Tag saveTag(@RequestBody Tag tag){
        articleService.saveTag(tag);
        return tag;
    }

    /**
     * 新增文章
     * @param article
     * @return
     */
    @PostMapping("/admin/article")
    @Transactional(rollbackFor = Exception.class)
    public String saveArticle(@RequestBody Article article){
        articleService.saveArticle(article);
        return "success";
    }

    /**
     * 获取文章列表
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/admin/articles/{status}/{startDate}/{endDate}")
    public List<Article> getArticles(@PathVariable String status,@PathVariable long startDate,@PathVariable long endDate){
        TimeRange timeRange=new TimeRange(startDate,endDate);
        List<Article> articles = articleService.getArticles(status,timeRange);
        return articles;
    }

    /**
     * 将文章失效不再展示
     * @param uid
     * @return
     */
    @DeleteMapping("/admin/article/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String terminalArticle(@PathVariable int uid){
        articleService.terminalArticle(uid);
        return "success";
    }
}
