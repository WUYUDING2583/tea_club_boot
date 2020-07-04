package com.yuyi.tea.controller;

import com.yuyi.tea.bean.*;
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

    /**
     * 根据门店id获取该门店产品列表
     * 目前未实现产品按门店分类
     * 故只获取所有产品
     * @param shopId
     * @return
     */
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
        List<ProductSale> list=new ArrayList<>();
        int length=3;
        if(products.size()<3){
            length=products.size();
        }
        for(int i=0;i<length;i++){
            products.get(i).getProduct().setProductDetails(null);
            Photo photo = products.get(i).getProduct().getPhotos().get(0);
            List<Photo> photos=new ArrayList<>();
            photos.add(photo);
            products.get(i).getProduct().setPhotos(photos);
            list.add(products.get(i));
        }
        return list;
    }

    /**
     * 微信小程序获取产品列表
     * @return
     */
    @GetMapping("/mp/product")
    public List<Product> getMpProducts(){
        List<Product> products=productService.getProducts();
        for(Product product:products){
            product.setActivities(null);
            product.setActivityRules(null);
            Photo photo = product.getPhotos().get(0);
            List<Photo> photos=new ArrayList<>();
            photos.add(photo);
            product.setPhotos(photos);
            product.setProductDetails(null);
        }
        return products;
    }

    /**
     * 包厢扫码获取商品列表
     * @return
     */
    @GetMapping("/mp/boxProduct")
    public List<Product> getMpBoxProducts(){
        List<Product> products=productService.getProducts();
        return products;
    }


    @GetMapping("/mp/product/{uid}")
    public Product getMpProduct(@PathVariable int uid){
        Product product = productService.getProduct(uid);
        if(product.getShop()!=null){
            product.getShop().setShopBoxes(null);
            product.getShop().setClerks(null);
            product.getShop().setPhotos(null);
            product.getShop().setOpenHours(null);
        }
        for (Activity activity : product.getActivities()) {
            activity.setPhotos(null);
        }

        for(ActivityRule activityRule:product.getActivityRules()){
            activityRule.setActivityApplyForProduct(null);
            activityRule.getActivity().setActivityRules(null);
            activityRule.getActivity().setPhotos(null);
        }
        return product;
    }
}