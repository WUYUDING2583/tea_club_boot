package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Article;
import com.yuyi.tea.bean.Tag;
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
    @Insert("insert into article(name,url,time,enforceTerminal) values(#{name},#{url},#{time},#{enforceTerminal})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveArticle(Article article);

    //保存文章标签
    @Insert("insert into articleTag(articleId,tagId) values(#{articleId},#{tagId})")
    void saveArticleTag(int tagId, int articleId);

    //获取文章列表
    @Select("select * from article")
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
    List<Article> getArticles();

    //根据uid获取该文章的标签
    @Select("select * from tag where uid in (" +
            "select tagId from articleTag where articleId=#{articleId}" +
            ")")
    List<Tag> getArticleTags(int articleId);

    //将文章失效不再展示
    @Update("update article set enforceTerminal=true where uid=#{uid}")
    void terminalArticle(int uid);
}
