package com.yuyi.tea.service;

import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ProductType;
import com.yuyi.tea.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "product")
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Cacheable(key = "'productTypes'")
    public List<ProductType> getProductTypes(){
        List<ProductType> productTypes = productMapper.getProductTypes();
        return productTypes;
    }

    //获取所有产品的名称
    @Cacheable(key = "'productsName'")
    public List<Product> getProductsName() {
        List<Product> productsNameByType = productMapper.getProductsName();
        return productsNameByType;
    }
}
