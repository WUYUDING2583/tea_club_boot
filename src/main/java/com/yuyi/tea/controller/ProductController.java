package com.yuyi.tea.controller;

import com.yuyi.tea.bean.ActivityRule;
import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ProductSale;
import com.yuyi.tea.bean.ProductType;
import com.yuyi.tea.service.ProductService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    /**
     * 创建新的产品种类
     * @param productType
     * @return
     */
    @PostMapping("/admin/productType")
    @Transactional(rollbackFor = Exception.class)
    public ProductType saveProductType(@RequestBody ProductType productType){
        productService.saveProductType(productType);
        return productType;
    }

    /**
     * 创建商品
     * @param product
     * @return
     */
    @PostMapping("/admin/product")
    @Transactional(rollbackFor = Exception.class)
    public String saveProduct(@RequestBody Product product){
        productService.saveProduct(product);
        return "success";
    }

    /**
     * 获取产品列表
     * @return
     */
    @GetMapping("/admin/products")
    public List<Product> getProducts(){
        List<Product> products = productService.getProducts();
        return products;
    }

    /**
     * 下架商品
     * @param uid
     * @return
     */
    @DeleteMapping("/admin/product/{uid}")
    @Transactional(rollbackFor = Exception.class)
    public String terminalProduct(@PathVariable int uid){
        productService.terminalProduct(uid);
        return "success";
    }

    /**
     * 根据uid获取产品信息
     * @param uid
     * @return
     */
    @GetMapping("/admin/product/{uid}")
    public Product getProduct(@PathVariable int uid){
        Product product = productService.getProduct(uid);
        return product;
    }

    /**
     * 修改产品信息
     * @param product
     * @return
     */
    @PutMapping("/admin/product")
    @Transactional(rollbackFor = Exception.class)
    public Product updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }

    @GetMapping("/mobile/products/{shopId}")
    public List<Product> getShopProductList(@PathVariable int shopId){
        List<Product> products=productService.getProducts(shopId);
        for(Product product:products){
            List<ActivityRule> filterRules=new ArrayList<>();
            for(ActivityRule activityRule:product.getActivityRules()){
                activityRule.setActivityApplyForProduct(null);
                activityRule.getActivity().setPhotos(null);
                activityRule.getActivity().setActivityRules(null);
                if(activityRule.getActivityRuleType().getName().equals("折扣")||activityRule.getActivityRuleType().getName().equals("购物")){
                    filterRules.add(activityRule);
                }
            }
            product.setActivityRules(filterRules);
        }
        return products;
    }

    /**
     * 获取最近一月销量最多的产品
     * @return
     */
    @GetMapping("/mp/product/hot")
    public List<ProductSale> getHotProduct(){
        List<ProductSale> products=productService.getHotPorducts();
        return products;
    }
}