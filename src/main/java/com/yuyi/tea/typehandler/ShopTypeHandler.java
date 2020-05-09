package com.yuyi.tea.typehandler;

import com.yuyi.tea.bean.Shop;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Shop.class)
@MappedJdbcTypes(JdbcType.INTEGER)
@Slf4j
public class ShopTypeHandler implements TypeHandler<Shop> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Shop shop, JdbcType jdbcType) throws SQLException {
        log.info("shop【"+shop+"】");
        preparedStatement.setObject(i,shop);
    }

    @Override
    public Shop getResult(ResultSet resultSet, String s) throws SQLException {
        Shop result= new Shop( resultSet.getInt(s));
        log.info("读取参数1【"+result+"】");
        return result;
    }

    @Override
    public Shop getResult(ResultSet resultSet, int i) throws SQLException {
        Shop result= new Shop(resultSet.getInt(i));
        log.info("读取参数2【"+result+"】");
        return result;
    }

    @Override
    public Shop getResult(CallableStatement callableStatement, int i) throws SQLException {
        Shop result= new Shop(callableStatement.getInt(i));
        log.info("读取参数1【"+result+"】");
        return result;
    }
}
