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
    public static String REDIS_PRODUCT_NAME=REDIS_PRODUCTS_NAME+":product";

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

    /**
     * 创建新的产品种类
     * @param productType
     */
    public void saveProductType(ProductType productType) {
        productMapper.saveProductType(productType);
        boolean hasKey=redisService.exists(REDIS_PRODUCT_TYPES_NAME);
        if(hasKey){
            log.info("更新redis中产品列表信息");
            List<ProductType> productTypes= (List<ProductType>) redisService.get(REDIS_PRODUCT_TYPES_NAME);
            productTypes.add(productType);
            redisService.set(REDIS_PRODUCT_TYPES_NAME,productTypes);
        }
    }

    /**
     * 创建商品
     * @param product
     */
    public void saveProduct(Product product) {
        priceMapper.savePrice(product.getPrice());
        productMapper.saveProduct(product);
        for(Photo photo:product.getPhotos()){
            photo.setProductId(product.getUid());
            photoMapper.saveProductPhoto(photo);
        }
    }

    /**
     * 获取产品列表
     * @return
     */
    public List<Product> getProducts() {
        List<Product> products = productMapper.getProducts();
        return products;
    }

    /**
     * 下架商品
     * @param uid
     */
    public void terminalProduct(int uid) {
        productMapper.terminalProduct(uid);
        boolean hasKey=redisService.exists(REDIS_PRODUCT_NAME+":"+uid);
        if(hasKey){
            log.info("更新redis中产品状态");
            Product product= (Product) redisService.get(REDIS_PRODUCT_NAME+":"+uid);
            product.setEnforceTerminal(true);
            redisService.set(REDIS_PRODUCT_NAME+":"+uid,product);
        }
    }

    /**
     * 根据uid获取产品信息
     * @param uid
     * @return
     */
    public Product getProduct(int uid) {
        boolean hasKey=redisService.exists(REDIS_PRODUCT_NAME+":"+uid);
        Product product;
        if(hasKey){
            product= (Product) redisService.get(REDIS_PRODUCT_NAME+":"+uid);
            log.info("从redis中获取产品详情"+product);
        }else{
            log.info("从数据库中获取产品详情");
            product= productMapper.getProduct(uid);
            log.info("将产品详情存入redis"+product);
            redisService.set(REDIS_PRODUCT_NAME+":"+uid,product);
        }
        return product;
    }

    /**
     * 修改产品信息
     * @param product
     * @return
     */
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
        boolean hasKey=redisService.exists(REDIS_PRODUCT_NAME+":"+product.getUid());
        if(hasKey){
            log.info("更新redis中产品信息");
            Product redisProduct= (Product) redisService.get(REDIS_PRODUCT_NAME+":"+product.getUid());
            product.setActivityRules(redisProduct.getActivityRules());
            product.setActivities(redisProduct.getActivities());
            redisService.set(REDIS_PRODUCT_NAME+":"+product.getUid(),product);
        }
        return product;
    }

    /**
     * 从缓存中获取产品信息
     * @param uid
     * @return
     */
    public Product getRedisProduct(int uid){
        boolean hasKey = redisService.exists(REDIS_PRODUCT_NAME+":"+uid);
        Product product=null;
        if(hasKey){
            //获取缓存
            product= (Product) redisService.get(REDIS_PRODUCT_NAME+":"+uid);
            log.info("从缓存获取的数据"+ product);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            product = productMapper.getProduct(uid);
            activityService.clearActivityRules(product.getActivityRules());
            activityService.clearActivities(product.getActivities());
            redisService.set(REDIS_PRODUCT_NAME+":"+uid,product);
            log.info("数据插入缓存" + product);
        }
        return product;
    }


    /**
     * 获取门店产品列表
     * @param shopId
     * @return
     */
    public List<Product> getProducts(int shopId) {
        List<Product> products=productMapper.getShopProducts(shopId);
        return products;
    }

    /**
     * 获取小程序走马灯展示的产品
     * @return
     */
    public List<Product> getSwiperList() {
        List<Product> products=productMapper.getSwiperList();
        return products;
    }

    /**
     * 获取最近一月销量最多的产品
     * @return
     */
    public List<ProductSale> getHotPorducts() {
        List<ProductSale> products=productMapper.getHotProducts();
        for(ProductSale productSale:products){
            productSale.getProduct().setActivityRules(null);
            productSale.getProduct().setActivities(null);
            productSale.getProduct().setShop(null);
        }
        return products;
    }
}
