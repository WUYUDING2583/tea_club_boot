package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Company;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CompanyMapper {
//    private int uid;
//    private String companyName;
//    private String postCode;
//    private String contact;
//    private String websiteName;
//    private String weChatOfficialAccount;
//    private String address;
    @Select("select * from company limit 1")
    Company getCompany();

    @Update("update company set companyName=#{companyName},postCode=#{postCode},contact=#{contact},websiteName=#{websiteName},weChatOfficialAccount=#{weChatOfficialAccount},address=#{address} where uid=#{uid}")
    void updateCompany(Company company);

    @Delete("delete from Company where uid=#{uid}")
    void deleteCompany(int uid);

    @Insert("insert into Company(companyName,postCode,contact,websiteName,weChatOfficialAccount,address) values(#{companyName},#{postCode},#{contact},#{websiteName},#{weChatOfficialAccount},#{address}) ")
    void insertCompany(Company company);

}
