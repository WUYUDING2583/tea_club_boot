package com.yuyi.tea.service;

import com.yuyi.tea.bean.Article;
import com.yuyi.tea.bean.Tag;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.common.TimeRange;
import com.yuyi.tea.mapper.ArticleMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleService {

    public static final String REDIS_ARTICLES_NAME="articles";
    public static final String REDIS_TAGS_NAME=REDIS_ARTICLES_NAME+":tags";
    public static final String REDIS_ARTICLE_NAME=REDIS_ARTICLES_NAME+";article";

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 获取文章标签列表
     * @return
     */
    public List<Tag> getTags() {
        List<Tag> tags=null;
        boolean hasKey=redisService.exists(REDIS_TAGS_NAME);
        if(hasKey){
            tags= (List<Tag>) redisService.get(REDIS_TAGS_NAME);
            log.info("从缓存中读取文章标签列表"+tags);
        }else{
            log.info("从数据库读取文章标签列表");
            tags=articleMapper.getTags();
            redisService.set(REDIS_TAGS_NAME,tags);
            log.info("将文章标签列表存入缓存"+tags);
        }
      return tags;
    }

    /**
     * 新增文章标签
     * @param tag
     */
    public void saveTag(Tag tag) {
        articleMapper.saveTag(tag);
        boolean hasKey=redisService.exists(REDIS_TAGS_NAME);
        if(hasKey) {
            List<Tag> redisTags = (List<Tag>) redisService.get(REDIS_TAGS_NAME);
            redisTags.add(tag);
            log.info("更新缓存中文章标签列表" + redisTags);
            redisService.set(REDIS_TAGS_NAME, redisTags);
        }
    }

    /**
     * 新增文章
     * @param article
     */
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

    /**
     * 获取文章列表
     * @param status
     * @param timeRange
     * @return
     */
    public List<Article> getArticles(String status, TimeRange timeRange) {
        List<Article> articles = articleMapper.getArticles(status,timeRange);
        for(Article article:articles){
            article.setPhoto(null);
        }
        return articles;
    }

    /**
     * 将文章失效不再展示
     * @param uid
     */
    public void terminalArticle(int uid) {
        articleMapper.terminalArticle(uid);
        boolean hasKey=redisService.exists(REDIS_ARTICLE_NAME+":"+uid);
        if(hasKey){
            log.info("更新redis中文章状态");
            Article redisArticle= (Article) redisService.get(REDIS_ARTICLE_NAME+":"+uid);
            redisArticle.setEnforceTerminal(true);
            redisService.set(REDIS_ARTICLE_NAME+":"+uid,redisArticle);
        }
    }
}
