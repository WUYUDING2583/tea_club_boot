package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ProductType;
import com.yuyi.tea.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //获取所有产品类型
    @GetMapping("/productTypes")
    public List<ProductType> getProductTypes(){
        List<ProductType> productTypes = productService.getProductTypes();
        return productTypes;
    }

    //根据所有产品的名称
    @GetMapping("/productsName")
    public List<Product> getProducts(){
        List<Product> productsNameByType = productService.getProductsName();
        return productsNameByType;
    }
}
