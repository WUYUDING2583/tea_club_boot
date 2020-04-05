package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Authority;
import com.yuyi.tea.bean.AuthorityFront;
import com.yuyi.tea.bean.Clerk;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface LoginMapper {



    //获取权限详情
    @Select("select * from authorityFront")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "belong",property = "belong",
                    one = @One(select="com.yuyi.tea.mapper.LoginMapper.getAuthority",
                            fetchType = FetchType.LAZY))
    })
    List<AuthorityFront> getAuthorities();

    //获取权限所属项
    @Select("select * from authority where uid=#{uid}")
    Authority getAuthority(int uid);
}
