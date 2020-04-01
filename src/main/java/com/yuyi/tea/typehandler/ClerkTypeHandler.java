package com.yuyi.tea.typehandler;

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

@MappedTypes(Clerk.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class ClerkTypeHandler implements TypeHandler<Clerk> {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Clerk clerk, JdbcType jdbcType) throws SQLException {
        log.info("设置clerk参数【"+clerk+"】");
        preparedStatement.setObject(i,clerk);
    }

    @Override
    public Clerk getResult(ResultSet resultSet, String s) throws SQLException {
        Clerk result= new Clerk( resultSet.getInt(s));
        log.info("读取参数1【"+result+"】");
        return result;
    }

    @Override
    public Clerk getResult(ResultSet resultSet, int i) throws SQLException {
        Clerk result= new Clerk(resultSet.getInt(i));
        log.info("读取参数2【"+result+"】");
        return result;
    }

    @Override
    public Clerk getResult(CallableStatement callableStatement, int i) throws SQLException {
        Clerk result= new Clerk(callableStatement.getInt(i));
        log.info("读取参数1【"+result+"】");
        return result;
    }
}
