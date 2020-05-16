package com.yuyi.tea.typehandler;

import com.yuyi.tea.bean.Product;
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

@MappedJdbcTypes(JdbcType.INTEGER)
@MappedTypes(Product.class)
public class ProductTypeHandler implements TypeHandler<Product> {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Product product, JdbcType jdbcType) throws SQLException {
        log.info("设置product参数【"+product+"】");
        preparedStatement.setObject(i,product);
    }

    @Override
    public Product getResult(ResultSet resultSet, String s) throws SQLException {
        Product result= new Product( resultSet.getInt(s));
        log.info("读取参数1【"+result+"】");
        return result;
    }

    @Override
    public Product getResult(ResultSet resultSet, int i) throws SQLException {
        Product result= new Product(resultSet.getInt(i));
        log.info("读取参数2【"+result+"】");
        return result;
    }

    @Override
    public Product getResult(CallableStatement callableStatement, int i) throws SQLException {
        Product result= new Product(callableStatement.getInt(i));
        log.info("读取参数1【"+result+"】");
        return result;
    }
}
