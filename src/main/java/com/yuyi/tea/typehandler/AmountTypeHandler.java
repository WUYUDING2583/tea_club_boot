package com.yuyi.tea.typehandler;

import com.yuyi.tea.common.Amount;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AmountTypeHandler implements TypeHandler<Amount> {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Amount amount, JdbcType jdbcType) throws SQLException {
        log.info("amount【"+amount+"】");
        preparedStatement.setObject(i,amount);
    }

    @Override
    public Amount getResult(ResultSet resultSet, String s) throws SQLException {
        Amount result= new Amount( );
        if(s.equals("ingot")){
            result.setIngot(resultSet.getFloat(s));
        }else{
            result.setCredit(resultSet.getInt(s));
        }
        log.info("读取参数1【"+result+"】");
        return result;
    }


    @Override
    public Amount getResult(ResultSet resultSet, int i) throws SQLException {
      return null;
    }

    @Override
    public Amount getResult(CallableStatement callableStatement, int i) throws SQLException {
      return null;
    }
}
