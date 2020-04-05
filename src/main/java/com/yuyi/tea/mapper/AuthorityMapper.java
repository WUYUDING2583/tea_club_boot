package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface AuthorityMapper {

    /**
     * 获取后端路由
     * @param uid
     * @return
     */
    @Select("select * from authorityEnd where uid=#{uid}")
    @Results(id="authorityEnd",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column="belongFront",property="belongFront",
                            one=@One(select="com.yuyi.tea.mapper.AuthorityMapper.getAuthorityFront",
                                    fetchType= FetchType.LAZY)),

            }
    )
    AuthorityEnd getAuthorityEnd(int uid);

    /**
     * 根据url和请求方法类型获取对应的后端路由
     * @param url
     * @param method
     * @return
     */
    @Select("select * from authorityEnd where url=#{url} and method=#{method}")
    @ResultMap("authorityEnd")
    AuthorityEnd getAuthorityEndByUrlMethod(String url,String method);


    /**
     * 根据前端路由id获取对应的后端路由列表
     * @param belongFront
     * @return
     */
    @Select("select * from authorityEnd where belongFront=#{belongFront}")
    List<AuthorityEnd> getAuthorityEndByFront(int belongFront);


    /**
     * 获取前端路由列表
     * @return
     */
    @Select("select * from authorityFront")
    @Results(id="authorityFront",
            value = {
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "belong",property = "belong",
                    one = @One(select="com.yuyi.tea.mapper.AuthorityMapper.getAuthority",
                            fetchType = FetchType.LAZY)),
            @Result(column = "uid",property = "authorityEnds",
                    one = @One(select="com.yuyi.tea.mapper.AuthorityMapper.getAuthorityEndByFront",
                            fetchType = FetchType.LAZY)),
    })
    List<AuthorityFront> getAuthorityFronts();

    /**
     * 获取前端路由所属大类
     * @param uid
     * @return
     */
    @Select("select * from authority where uid=#{uid}")
    Authority getAuthority(int uid);

    /**
     * 获取前端路由
     * @param uid
     * @return
     */
    @Select("select * from authorityFront where uid=#{uid}")
    @ResultMap("authorityFront")
    AuthorityFront getAuthorityFront(int uid);

    /**
     * 根据职位id和后端路由id获取对应职位的后端路由信息
     * @param positionId
     * @param authorityEndId
     * @return
     */
    @Select("select * from positionAuthorityEndDetail where positionId=#{positionId} and authorityEndId=#{authorityEndId}")
    @Results(id="positionAuthorityEndDetail",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column = "positionId",property = "position",
                            one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getPosition",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "authorityEndId",property = "authorityEnd",
                            one = @One(select="com.yuyi.tea.mapper.AuthorityMapper.getAuthorityEnd",
                                    fetchType = FetchType.LAZY)),
            })
    PositionAutorityEndDetail getPositionAutorityEndDetail(int positionId, int authorityEndId);

    /**
     * 根据positionId获取职位对应的前端路由权限
     * @param positionId
     * @return
     */
    @Select("select * from positionAuthorityFrontDetail where positionId=#{positionId}")
    @Results(id="positionAuthorityFrontDetail",
            value = {
                    @Result(id = true,column = "uid",property = "uid"),
                    @Result(column = "positionId",property = "position",
                            one = @One(select="com.yuyi.tea.mapper.ClerkMapper.getPosition",
                                    fetchType = FetchType.LAZY)),
                    @Result(column = "authorityFrontId",property = "authorityFront",
                            one = @One(select="com.yuyi.tea.mapper.AuthorityMapper.getAuthorityFront",
                                    fetchType = FetchType.LAZY)),
            })
    List<PositionAutorityFrontDetail> getPositionAutorityFrontDetails(int positionId);
}
