package com.yuyi.tea.typehandler;

import com.yuyi.tea.bean.ActivityRule;
import com.yuyi.tea.bean.Clerk;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ActivityRule.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class ActivityRuleTypeHandler implements TypeHandler<ActivityRule> {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, ActivityRule activityRule, JdbcType jdbcType) throws SQLException {
        log.info("设置activityRule参数【"+activityRule+"】");
        preparedStatement.setObject(i,activityRule);
    }

    @Override
    public ActivityRule getResult(ResultSet resultSet, String s) throws SQLException {
        ActivityRule result= new ActivityRule( resultSet.getInt(s));
        log.info("读取参数1【"+result+"】");
        return result;
    }

    @Override
    public ActivityRule getResult(ResultSet resultSet, int i) throws SQLException {
        ActivityRule result= new ActivityRule(resultSet.getInt(i));
        log.info("读取参数2【"+result+"】");
        return result;
    }

    @Override
    public ActivityRule getResult(CallableStatement callableStatement, int i) throws SQLException {
        ActivityRule result= new ActivityRule(callableStatement.getInt(i));
        log.info("读取参数1【"+result+"】");
        return result;
    }
}
