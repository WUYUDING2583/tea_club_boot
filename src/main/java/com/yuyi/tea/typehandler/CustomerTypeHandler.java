package com.yuyi.tea.typehandler;

import com.yuyi.tea.bean.Customer;
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

@MappedTypes(Customer.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class CustomerTypeHandler implements TypeHandler<Customer> {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Customer customer, JdbcType jdbcType) throws SQLException {
        log.info("设置customer参数【"+customer+"】");
        preparedStatement.setObject(i,customer);
    }

    @Override
    public Customer getResult(ResultSet resultSet, String s) throws SQLException {
        Customer result= new Customer( resultSet.getInt(s));
        log.info("读取参数1【"+result+"】");
        return result;
    }

    @Override
    public Customer getResult(ResultSet resultSet, int i) throws SQLException {
        Customer result= new Customer(resultSet.getInt(i));
        log.info("读取参数2【"+result+"】");
        return result;
    }

    @Override
    public Customer getResult(CallableStatement callableStatement, int i) throws SQLException {
        Customer result= new Customer(callableStatement.getInt(i));
        log.info("读取参数1【"+result+"】");
        return result;
    }
}
