package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Article;
import com.yuyi.tea.bean.Tag;
import com.yuyi.tea.common.TimeRange;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ArticleMapper {

    //获取文章标签列表
    @Select("select * from tag")
    List<Tag> getTags();

    //新增文章标签
    @Insert("insert into tag(name) values(#{name})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveTag(Tag tag);

    //新增文章
    @Insert("insert into article(title,description,url,time,enforceTerminal) values(#{title},#{description},#{url},#{time},#{enforceTerminal})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveArticle(Article article);

    //保存文章标签
    @Insert("insert into articleTag(articleId,tagId) values(#{articleId},#{tagId})")
    void saveArticleTag(int tagId, int articleId);

    /**
     * 获取文章列表
     * @param status 文章状态 all 所有状态 valid 未撤销文章 invalid 已撤销文章
     * @param timeRange
     * @return
     */
    @Select("<script>" +
            "select * from article where " +
            "<if test='status!=\"all\"'>" +
                "<if test='status==\"valid\"'>enforceTerminal=false and </if>" +
                "<if test='status==\"invalid\"'>enforceTerminal=true and </if>" +
            "</if>" +
            "<if test='timeRange.startDate!=-1'> <![CDATA[time>= #{timeRange.startDate}]]> and </if>" +
            "<![CDATA[time<=#{timeRange.endDate}]]> order by time desc" +
            "</script>")
    @Results(id="article",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column="uid",property="tags",
                            one=@One(select="com.yuyi.tea.mapper.ArticleMapper.getArticleTags",
                                    fetchType= FetchType.LAZY)),
                    @Result(column="uid",property="photo",
                            one=@One(select="com.yuyi.tea.mapper.PhotoMapper.getPhotoByArticleId",
                                    fetchType= FetchType.LAZY)),

            }
    )
    List<Article> getArticles(String status, TimeRange timeRange);

    //根据uid获取该文章的标签
    @Select("select * from tag where uid in (" +
            "select tagId from articleTag where articleId=#{articleId}" +
            ")")
    List<Tag> getArticleTags(int articleId);

    //将文章失效不再展示
    @Update("update article set enforceTerminal=true where uid=#{uid}")
    void terminalArticle(int uid);

    /**
     * 获取小程序走马灯展示的文章
     * @return
     */
    @Select("select * from article where isShowOnHome=true")
    @ResultMap("article")
    List<Article> getSwiperList();

    @Select("select * from article where title like #{value} or description like #{value}")
    @ResultMap("article")
    List<Article> search(String value);

    /**
     * 小程序获取文章
     * @param articleId
     * @return
     */
    @Select("select * from article where uid=#{uid}")
    @ResultMap("article")
    Article getArticle(int articleId);
}
