package com.yuyi.tea.service;

import com.yuyi.tea.bean.Article;
import com.yuyi.tea.bean.Tag;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.mapper.ArticleMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private RedisService redisService;

    //获取文章标签列表
    public List<Tag> getTags() {
        List<Tag> tags=null;
        boolean hasKey=redisService.exists("article:tags");
        if(hasKey){
            tags= (List<Tag>) redisService.get("article:tags");
            log.info("从缓存中读取文章标签列表"+tags);
        }else{
            log.info("从数据库读取文章标签列表");
            tags=articleMapper.getTags();
            redisService.set("article:tags",tags);
            log.info("将文章标签列表存入缓存"+tags);
        }
      return tags;
    }

    //新增文章标签
    public void saveTag(Tag tag) {
        articleMapper.saveTag(tag);
        List<Tag> redisTags = (List<Tag>) redisService.get("article:tags");
        redisTags.add(tag);
        log.info("更新缓存中文章列表"+redisTags);
        redisService.set("article:tags",redisTags);
    }

    //新增文章
    public void saveArticle(Article article) {
        long time= TimeUtil.getCurrentTimestamp();
        article.setTime(time);
        //保存文章信息
        articleMapper.saveArticle(article);
        //保存图片
        article.getPhoto().setArticleId(article.getUid());
        photoMapper.saveArticlePhoto(article.getPhoto());
        //保存文章标签
        for(Tag tag:article.getTags()){
            articleMapper.saveArticleTag(tag.getUid(),article.getUid());
        }
    }

    //获取文章列表
    public List<Article> getArticles() {
        List<Article> articles = articleMapper.getArticles();
        for(Article article:articles){
            article.setPhoto(null);
        }
        return articles;
    }

    //将文章失效不再展示
    public void terminalArticle(int uid) {
        articleMapper.terminalArticle(uid);
    }
}
