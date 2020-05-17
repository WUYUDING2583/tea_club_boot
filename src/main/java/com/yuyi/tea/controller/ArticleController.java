package com.yuyi.tea.controller;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.TimeRange;
import com.yuyi.tea.common.utils.TimeUtil;
import com.yuyi.tea.service.ActivityService;
import com.yuyi.tea.service.ArticleService;
import com.yuyi.tea.service.ProductService;
import com.yuyi.tea.service.ShopBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ShopBoxService shopBoxService;

    @Autowired
    private ProductService productService;

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
        for(Article article:articles){
            article.setPhoto(null);
        }
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


    /**
     * 获取小程序走马灯展示的文章，活动，产品，包厢
     * @return
     */
    @GetMapping("/mp/swipers")
    public List<Object> getSwiperList(){
        List<Article> articles=articleService.getSwiperList();
        List<Activity> activities=activityService.getSwiperList();
        List<ShopBox> shopBoxes=shopBoxService.getSwiperList();
        List<Product> products=productService.getSwiperList();
        List<Object> list=new ArrayList<>();
        if(articles!=null) {
            for (Article article : articles) {
                list.add(article.getPhoto());
            }
        }
        if(activities!=null) {
            for (Activity activity : activities) {
                list.add(activity.getPhotos().get(0));
            }
        }
        if(shopBoxes!=null) {
            for (ShopBox shopBox : shopBoxes) {
                list.add(shopBox.getPhotos().get(0));
            }
        }
        if(products!=null) {
            for (Product product : products) {
                list.add(product.getPhotos().get(0));
            }
        }
        return list;
    }

    /**
     * 获取所有有效文章
     * @return
     */
    @GetMapping("/mp/article")
    public List<Article> getArticles(){
        TimeRange timeRange=new TimeRange(-1, TimeUtil.getCurrentTimestamp());
        List<Article> articles = articleService.getArticles("valid", timeRange);
        return articles;
    }
}
