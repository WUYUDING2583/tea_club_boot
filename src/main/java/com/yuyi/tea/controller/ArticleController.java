package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Article;
import com.yuyi.tea.bean.Tag;
import com.yuyi.tea.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //获取文章标签列表
    @GetMapping("/tags")
    public List<Tag> getTags(){
        List<Tag> tags = articleService.getTags();
        return tags;
    }

    //新增文章标签
    @PostMapping("/tag")
    @Transactional(rollbackFor = Exception.class)
    public Tag saveTag(@RequestBody Tag tag){
        articleService.saveTag(tag);
        return tag;
    }

    //新增文章
    @PostMapping("/article")
    @Transactional(rollbackFor = Exception.class)
    public String saveArticle(@RequestBody Article article){
        articleService.saveArticle(article);
        return "success";
    }

    //获取文章列表
    @GetMapping("/articles")
    public List<Article> getArticles(){
        List<Article> articles = articleService.getArticles();
        return articles;
    }

    //将文章失效不再展示
    @DeleteMapping("/article/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String terminalArticle(@PathVariable int uid){
        articleService.terminalArticle(uid);
        return "success";
    }
}
