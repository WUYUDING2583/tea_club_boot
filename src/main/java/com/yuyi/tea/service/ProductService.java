package com.yuyi.tea.service;

import com.yuyi.tea.bean.*;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.PriceMapper;
import com.yuyi.tea.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@CacheConfig(cacheNames = "product")
@Service
@Slf4j
public class ProductService {

    public static String REDIS_PRODUCTS_NAME="products";
    public static String REDIS_PRODUCT_TYPES_NAME=REDIS_PRODUCTS_NAME+":productTypes";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ActivityService activityService;


    /**
     * 获取所有产品类型
     * @return
     */
    public List<ProductType> getProductTypes(){
        boolean hasKey=redisService.exists(REDIS_PRODUCT_TYPES_NAME);
        List<ProductType> productTypes;
        if(hasKey){
            productTypes= (List<ProductType>) redisService.get(REDIS_PRODUCT_TYPES_NAME);
            log.info("从redis中获取产品类型列表"+productTypes);
        }else{
            log.info("从数据库中获取产品类型列表");
            productTypes = productMapper.getProductTypes();
            redisService.set(REDIS_PRODUCT_TYPES_NAME,productTypes);
            log.info("将产品类型列表存入redis"+productTypes);
        }
        return productTypes;
    }


    /**
     * 获取所有产品的名称
     * @return
     */
    public List<Product> getProductsNameAndType() {
        List<Product> productsNameByType = productMapper.getProductsNameAndType();
        return productsNameByType;
    }

    //创建新的产品种类
    public void saveProductType(ProductType productType) {
        productMapper.saveProductType(productType);
    }

    //创建新的产品
    public void saveProduct(Product product) {
        priceMapper.savePrice(product.getPrice());
        productMapper.saveProduct(product);
        for(Photo photo:product.getPhotos()){
            photo.setProductId(product.getUid());
            photoMapper.saveProductPhoto(photo);
        }
    }

    //获取产品列表
    public List<Product> getProducts() {
        List<Product> products = productMapper.getProducts();
        return products;
    }

    //下架商品
    public void terminalProduct(int uid) {
        productMapper.terminalProduct(uid);
    }

    //根据uid获取产品信息
    public Product getProduct(int uid) {
        Product product = productMapper.getProduct(uid);
        return product;
    }

    //修改产品信息
    public Product updateProduct(Product product) {
        productMapper.updateProduct(product);
        priceMapper.updatePrice(product.getPrice());
        ArrayList<Integer> photosUid=new ArrayList<Integer>();
        //先更新包厢照片数据
        for(Photo photo:product.getPhotos()){
            photosUid.add(photo.getUid());
            photo.setProductId(product.getUid());
            photoMapper.saveProductPhoto(photo);
        }
        //获取目前所有该产品照片再过滤出过滤后要删除的照片
        List<Photo> originPhotos = photoMapper.getPhotosByProductId(product.getUid());
        List<Photo> needDeletePhotos=originPhotos.stream()
                .filter((Photo photo)->!photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        //删除这些照片
        for(Photo photo:needDeletePhotos){
            photoMapper.deletePhoto(photo.getUid());
        }
        List<Photo> currentPhotos = originPhotos.stream()
                .filter((Photo photo)->photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        product.setPhotos(currentPhotos);
        return product;
    }

    //从缓存中获取产品信息
    public Product getRedisProduct(int uid){
        boolean hasKey = redisService.exists("products:product:"+uid);
        Product product=null;
        if(hasKey){
            //获取缓存
            product= (Product) redisService.get("products:product:"+uid);
            log.info("从缓存获取的数据"+ product);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            product = productMapper.getProduct(uid);
            activityService.clearActivityRules(product.getActivityRules());
//            for(ActivityRule activityRule:product.getActivityRules()){
//                ActivityService.clearActivityRule(activityRule);
//            }
            activityService.clearActivities(product.getActivities());
//            for(Activity activity:product.getActivities()){
//                activity.setPhotos(null);
//                activity.setMutexActivities(null);
//                activity.setActivityRules(null);
//            }
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisService.set("products:product:"+uid,product);
            log.info("数据插入缓存" + product);
        }
        return product;
    }
}
