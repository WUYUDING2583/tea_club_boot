package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Clerk;
import org.apache.ibatis.annotations.Select;

public interface LoginMapper {

    //根据员工提供的身份证号获取员工信息
    @Select("select * from clerk where identityId=#{identityId}")
    Clerk getClerkByIdentityId(String identityId);
}
