package com.yuyi.tea.controller;

import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ProductType;
import com.yuyi.tea.service.ProductService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取所有产品类型
     * @return
     */
    @GetMapping("/admin/productTypes")
    public List<ProductType> getProductTypes(){
        List<ProductType> productTypes = productService.getProductTypes();
        return productTypes;
    }

    /**
     * 获取所有产品的名称
     * @return
     */
    @GetMapping("/admin/productsName")
    public List<Product> getProductsNameAndType(){
        List<Product> productsNameByType = productService.getProductsNameAndType();
        return productsNameByType;
    }

    //创建新的产品种类
    @PostMapping("/productType")
    @Transactional(rollbackFor = Exception.class)
    public ProductType saveProductType(@RequestBody ProductType productType){
        productService.saveProductType(productType);
        return productType;
    }

    //创建商品
    @PostMapping("/product")
    @Transactional(rollbackFor = Exception.class)
    public String saveProduct(@RequestBody Product product){
        productService.saveProduct(product);
        return "success";
    }

    //获取产品列表
    @GetMapping("/products")
    public List<Product> getProducts(){
        List<Product> products = productService.getProducts();
        return products;
    }

    //下架商品
    @DeleteMapping("/product/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String terminalProduct(@PathVariable int uid){
        productService.terminalProduct(uid);
        return "success";
    }

    //根据uid获取产品信息
    @GetMapping("/product/{uid}")
    public Product getProduct(@PathVariable int uid){
        Product product = productService.getProduct(uid);
        return product;
    }

    @PutMapping("/product")
    @Transactional(rollbackFor = Exception.class)
    public Product updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }
}