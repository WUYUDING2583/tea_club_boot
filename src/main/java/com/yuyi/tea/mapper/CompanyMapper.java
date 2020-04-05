package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.Company;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CompanyMapper {

    /**
     * 获取公司信息
     * @return
     */
    @Select("select * from company limit 1")
    Company getCompany();

    /**
     * 更新公司信息
     * @param company
     */
    @Update("update company set companyName=#{companyName},postCode=#{postCode},contact=#{contact},websiteName=#{websiteName},weChatOfficialAccount=#{weChatOfficialAccount},address=#{address},rechargeRate=#{rechargeRate} where uid=#{uid}")
    void updateCompany(Company company);

}
