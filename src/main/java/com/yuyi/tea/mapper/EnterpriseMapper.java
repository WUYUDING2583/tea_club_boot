package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Enterprise;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

public interface EnterpriseMapper {

    //根据uid获取企业信息
    @Select("select * from enterprise where uid=#{uid}")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "uid",property = "businessLicense",
                    one = @One(select="com.yuyi.tea.mapper.PhotoMapper.getBusinessLicenseByEnterpriseId",
                            fetchType = FetchType.LAZY))
    })
    Enterprise getEnterPriseByUid(int uid);
}
